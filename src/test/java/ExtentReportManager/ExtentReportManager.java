package ExtentReportManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentTest test;

    public static void initReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void createTest(String testName) {
        test = extent.createTest(testName);
    }

    public static void logInfo(String message) {
        if (test != null) {
            test.info(message);
        } else {
            System.out.println("[WARN] logInfo çağrıldı ama başarısız: " + message);
        }
    }

    public static void logPass(String message) {
        test.pass(message);
    }

    public static void logFail(String message) {
        if (test != null) {
            test.fail(message);
        } else {
            System.out.println("logFail çağrıldı ama başarısız: " + message);
        }
    }

    public static void logWarning(String message) {
        test.warning(message);
    }

    public static void logScreenshot(String path) {
        try {
            test.addScreenCaptureFromPath(path);
        } catch (Exception e) {
            test.warning("Screenshot yüklenemedi: " + e.getMessage());
        }
    }
}
