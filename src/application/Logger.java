package application;

public class Logger {
	private static Logger _logger = null;
	
	public static boolean debugIsOn = true;
	
	private Logger() {}
	
	public static Logger getInstance() {
		if (_logger == null) {
			_logger = new Logger();
		}
		return _logger;
	}
	
	public static void log(String message) {
		System.out.printf("Logger: %s\n", message);
	}
	
	public static void printLine(String message) {
		System.out.println(message);
	}	
	
	public static void print(String message) {
		System.out.print(message);
	}

	public static void debug(Object obj, String message) {
		if (debugIsOn) System.out.printf("Debugger: %s %s\n", 
				obj.getClass().getName(), message);
	}
}
