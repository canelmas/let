import java.util.Map;
import java.util.HashMap;

/**
 * The Trace aspect injects tracing messages before and after method main of
 * class HelloWorld.
 */

aspect Trace {

	pointcut methodCalls():
	  execution(* com.canelmas..*(..))&& !within(com.canelmas.permissioncompat.sample);

	Object around() : methodCalls() {

		String threadName = Thread.currentThread().getName();

		if(null==threadMap.get(threadName)){
			threadMap.put(threadName,0);
		}

		int stackDepth = threadMap.get(threadName) + 1;



		threadMap.put(threadName,stackDepth);


		String name = Thread.currentThread().getName();

		String indent = "";

		for (int index = 0; index < stackDepth; index++) {
			indent += "   ";
		}

		System.out.println(name+":"+indent + ">>>>    "
				+ thisJoinPointStaticPart.getSignature().toString());

		long start = System.currentTimeMillis();
		try {
			return proceed();
		} finally {

			long end = System.currentTimeMillis();

			System.out.println(name+":"+indent + "<<<< "
					+ thisJoinPointStaticPart.getSignature().toString() + "("
					+ (end - start) + " milliseconds)");

			threadMap.put(threadName,stackDepth - 1);
		}

	}

	private static Map<String, Integer> threadMap= new HashMap<String,Integer>();

}