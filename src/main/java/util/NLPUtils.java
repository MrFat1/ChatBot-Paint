package util;

import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.namefind.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import domain.JVentanaLienzo;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/** Clase dedicada a todas las herramientas necesarias para el funcionamiento del chatbot. */
public class NLPUtils {

    private static JVentanaLienzo JLienzo;
    private static ImageIcon icon = new ImageIcon("resources/icon-error.png");

    /**
     * Este metodo separa la frase introducida en palabras denomindadas "tokens" para su posterior analisis individual.
     * Adicionalmente, se pueden mostrar las probabilidades de detección de cada palabra.
     * Para mas detalles consultar los comentarios en el codigo.
     * @param frase Frase a tokenizar
     * @return Devuleve un array de tokens o palabras
     */
    public static String[] Tokenizar(String frase) {

        InputStream isTokenizer = null;
        TokenizerModel tokenModel = null;
        try {
            //Leemos el modelo pre-entrenado en un Stream (Obtenido de internet)
            isTokenizer = new FileInputStream("resources/es-token.bin");

            //Generamos un modelo tokenizador a partir del stream
            tokenModel = new TokenizerModel(isTokenizer);
        } catch (IOException e) {
            System.err.println("Error al tokenizar. ¿El fichero es-token.bin esta bien?");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        //Iniciamos el tokenizador
        TokenizerME tokenizer = new TokenizerME(tokenModel);

        //Tokenizamos la frase
        String[] tokens = tokenizer.tokenize(frase);


        //////////////  Debug ///////////////////////

        //Podemos ver las probabilidades de cada palabra tokenizada (Para ver en cuales esta seguro y en cuales no)

        /*double probs[] = tokenizer.getTokenProbabilities();

        for(int i = 0; i < tokens.length; i++){
            System.out.println(tokens[i] + ": " + probs[i]);
        }*/

        return tokens;

    }

    /**
     * Cogemos la frase ya tokenizada y obtenemos un tag para cada token (palabra) corespondiente
     * a su categoria gramatical (Verbo, Sustantivo, Adjetivo...). Esto facilitara que el "lemmatizer"
     * elimine los cambios en la palabra (Tiepo verbal, genero, modificador...) y devuelva la palabra base
     * que aparece en los diccionarios. (P.Ej: pintame -Verbo -> pintar).
     * Para mas detalles consultar los comentarios en el codigo.
     * @param tokens Array de palabras
     * @return Devuleve un array con los tags o categorias gramaticales de cada token
     */
    public static String[] PartOfSpeechTag(String[] tokens) {

        POSModel posModel = null;
        try {
            //Leemos el modelo pre-entrenado y generamos un modelo PartOfSpeech
            InputStream posModelIn = new FileInputStream("resources/es-pos.bin");
            posModel = new POSModel(posModelIn);
        } catch (IOException e) {
            System.err.println("Error al PartOfSpeechTag. ¿El fichero es-pos.bin esta bien?");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        //Lo cargamos
        POSTaggerME posTagger = new POSTaggerME(posModel);

        //Sacamos el array de tags
        String[] tags = posTagger.tag(tokens);

        return tags;
    }

    /**
     * A partir de cada token y su categoria gramatical (obtenida en el PartOfSpeechTag) obtenemos la palabra base
     * o infinitivo para evitar confusiones con las familias o tiempos verbales.
     * En la consola se puede observar todo el trabajo de fondo.
     * Para mas detalles consultar los comentarios en el código.
     * @param tokens Array de tokens a analizar
     * @param tags Array de tags con las categorías gramaticales de cada token
     * @return Devuelve un array de Strings
     */
    public static String[] Lemmatizar(String[] tokens, String[] tags) {

        LemmatizerME lemmatizer = null;
        try {
            InputStream model = new FileInputStream("resources/es-lemmatizer.bin");
            lemmatizer = new LemmatizerME(new LemmatizerModel(model));
        } catch (IOException e) {
            System.err.println("Error al lemmatizar. ¿El fichero es-lemmatizer.bin esta bien?");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // Sacamos los lemmas o las palabras base de cada token con ayuda de los tags
        String[] lemmas = lemmatizer.lemmatize(tokens, tags);

        ////////// Debug //////////////////

        System.out.println("PALABRA -TAG : LEMMA\n");
        for(int i = 0; i < tokens.length; i++){
            System.out.println(tokens[i] + " -" + tags[i] + " : " + lemmas[i]);
        }

        return lemmas;

    }

    /**
     * Este metodo se ejecuta al iniciar el programa
     * Este metodo inicializara el "entrenador" o "detector" de palabras. En nuestro caso nos interesa que sepa
     * distinguir que tipo de figura vamos a pintar, las coordenadas donde la pintaremos, el tamaño de
     * la figura, su color y si tendra o no relleno. En internet existen modelos pre-entrenados para sacar
     * nombres, localizaciones, fechas... pero en estos casos lo mejor es crearte tu propio NER. En mi caso,
     * he creado el fichero figuras-train.txt con unas 500 frases diferentes para que tenga suficientes ejemplos
     * y pueda predecir con exactitud. (Normalmente sulen tener hasta 15000 frases).
     * Para mas detalles consultar los comentarios en el codigo.
     */
    public static void NERTrain(){

        InputStreamFactory intxt = null;
        try {
            intxt = new MarkableFileInputStreamFactory(new File("resources/figuras-train.txt"));
        } catch (FileNotFoundException e1) {
            System.err.println("Error al leer el fichero. ¿El fichero figuras-train.txt esta bien?");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        ObjectStream sampleStream = null;
        try {
            sampleStream = new NameSampleDataStream(new PlainTextByLineStream(intxt, StandardCharsets.UTF_8));
        } catch (IOException e2) {
            e2.printStackTrace();
            System.err.println("Error en el ObjectStream");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        //Establecemos los parámetros para el entrenamiento. En este caso hemos hecho 80 iteraciones utilizando el algoritmo de bayes para entrenar. (Existen diferentes formas de entrenar)
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 80);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

        //Lo entrenamos pasandole el codec, el lenguage y el training dataset
        TokenNameFinderModel nameFinderModel = null;
        try {
            nameFinderModel = NameFinderME.train("es", null, sampleStream,
                    params, TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("Error al entrenar");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);;
        }

        // Serializamos el modelo en un .bin para no tener que estar entrenando cada vez que ejecutemos
        try {
            File output = new File("resources/ner-custom-figuras.bin");
            FileOutputStream outputStream = new FileOutputStream(output);
            nameFinderModel.serialize(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("Error al serializar el fichero ner-custom-figuras.bin");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

    }

    /**
     * Este metodo utilizara el modelo ner-custom-figuras.bin (entrenado en el método NERTrain()) para coger el array de
     * lemmas obtenido en el lemmatizer y asignar a cada lemma (palabra) su categoria mas adecuada. (P.Ej: circulo -> figura, verde -> color).
     * Posteriormente utilizaremos dicha categoria para decirle al programa todas las caracteristicas de la figura que deseamos pintar.
     * @param lemmas Array de lemmas
     * @return Devuelve un array de tipo Span que contiene la categoria de cada palabra
     */
    public static Span[] NamedEntityRecognition(String[] lemmas) {

        //Cargamos el modelo
        InputStream inputStream;
        TokenNameFinderModel model = null;
        try {
            inputStream = new FileInputStream("resources/ner-custom-figuras.bin");
            model = new TokenNameFinderModel(inputStream);
        } catch (IOException e) {
            System.err.println("¿El fichero ner-custom-figuras.bin esta bien? Alomejor se te ha olvidado ejecutar NERTrain() para generar el .bin");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        NameFinderME nameFinder = new NameFinderME(model);

        Span[] nameSpans = nameFinder.find(lemmas);

        return nameSpans;

    }

    /**
     * En este metodo entrenaremos el fichero "figuras-categorizer.txt", otro fichero creado por mi para asignar una
     * categoria a cada frase introducida por el usuarios para decidir que acción llevar a cabo. Normalmente se suelen usar
     * chatbots mas avanzados para esta tarea, por lo que al haber tenido que definir yo todas las frases posibles puede
     * que el bot no detecte ciertas ordenes.
     * En este caso pondremos CUTOFF_PARAM a un valor muy bajo ya que trabajamos con pocas muestras.
     * Utilizaremos BagOfWordsFeatureGenerator para que trate cada palabra como una "feature" es decir, cada palabra
     * es importante y has de tenerla en cuenta.
     * @throws IOException Lanza excepciones que son recogidas en otras partes del código para mayor comodidad.
     */
    public static void categorizerTrain() throws IOException {

        //Leemos el fichero
        ObjectStream<String> lineStream = null;
        try {
            InputStreamFactory inputFichero = new MarkableFileInputStreamFactory(new File("resources/figuras-categorizer.txt"));
            lineStream = new PlainTextByLineStream(inputFichero, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error al leer el fichero. ¿El fichero figuras-categorizer.txt esta bien?");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        TrainingParameters parametros = ModelUtil.createDefaultTrainingParameters();
        parametros.put(TrainingParameters.CUTOFF_PARAM, 1);
        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[]{new BagOfWordsFeatureGenerator()});

        DoccatModel model = DocumentCategorizerME.train("es", sampleStream, parametros, factory);

        model.serialize(new File("resources/cat-custom-figuras.bin"));

    }

    /**
     * Este método utilizara el modelo "cat-custom-figuras.bin" entrenado en el metodo categorizerTrain() para obtener
     * la acción mas adecuada a la frase introducida. (pintar, saludar, borrar...).
     * En la consola se puede observar todo el trabajo de fondo.
     * @param frase Frase a analizar
     * @return Devuelve un String con la categoría seleccionada.
     */
    public static String categorizar(String frase) {

        DoccatModel m = null;
        try {
            InputStream is = new FileInputStream("resources/cat-custom-figuras.bin");
            m = new DoccatModel(is);
        } catch (IOException e) {
            System.err.println("¿El fichero cat-custom-figuras.bin esta bien? Alomejor se te ha olvidado ejecutar categorizerTrain() para generar el .bin");
            JOptionPane.showMessageDialog(JLienzo, "Error interno, revisa la consola.", "Error", JOptionPane.INFORMATION_MESSAGE, icon);

        }
        DocumentCategorizerME cat = new DocumentCategorizerME(m);

        // Obtenemos las probabilidades de cada categoría anazlizando cada token
        double[] probs = cat.categorize(Tokenizar(frase));

        // Obtenemos la categería con mas prob
        String category = cat.getBestCategory(probs);

        System.out.println("//////////// DEBUG ///////////////");
        System.out.println("\n---------------------------------\nCategoría | Probabilidad\n---------------------------------");
        for(int i = 0; i < cat.getNumberOfCategories(); i++){
            System.out.println(cat.getCategory(i) + " : " + probs[i]);
        }
        System.out.println("---------------------------------");

        System.out.println("DEBUG: La categoría elegida es " + category);

        return category;

    }

}
