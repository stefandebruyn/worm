import java.io.File;
import java.io.IOException;

public abstract class worm {
  private static int successes = 0,
                     failures = 0,
                     levels = 0,
                     directories = 0;



  public static void main(String[] args) {
    // Enforce usage
    if (args.length != 1)
      abort("Usage: java worm <directory>");

    File input = new File(args[0]);

    if (!input.exists())
      abort("No such directory");

    if (!input.isDirectory())
      abort("\"" + args[0] + "\"" + " is not a directory");

    // Worm through directories
    long start = System.currentTimeMillis();

    worm(input);

    long end = System.currentTimeMillis();

    // Conclude
    System.out.printf("\nFinished in %.2fs\n" +
                      "Compiled %d files in %d directories (%d failures)\n", (end-start)/1000.0, successes, directories, failures);
  }



  private static void worm(File f) {
    // File is a directory
    if (f.isDirectory()) {
      out("Entering /" + f.getName());
      levels++;
      directories++;

      File[] tree = f.listFiles();
      for (File sub : tree)
        worm(sub);

      levels--;
    }

    // File is a .java
    else if (f.getPath().substring(f.getPath().lastIndexOf("."), f.getPath().length()).equals(".java")) {
      out("Compiling " + f.getName() + "... ");

      boolean success = true;

      try {
        Runtime.getRuntime().exec(new String[] { "javac", f.getPath() });
      } catch (IOException e) {
        System.out.print("Failed!");
        failures++;
        success = false;
      }

      if (success)
        successes++;
    }
  }



  private static void abort(String str) {
    System.out.println(str);
    System.exit(0);
  }



  private static String tabs() {
    String res = "";

    for (int i = 0; i < levels; i++)
      res += "    ";

    return res;
  }



  private static void out(String str) {
    System.out.println(tabs() + str);
  }
}
