package util;

/** Clase de funcionalidad variada que proporciona una ayuda al alumno */
public class Util
{
	/** 
		Detiene el programa el tiempo especificado
		@param segundos número de segundos a esperar 
	*/
	public static void wait(int segundos)
	{
		try
		{
			Thread.sleep(segundos*1000);
		}
		catch(Exception e)
		{
			System.out.println("Exception!");
		}
	}
}
