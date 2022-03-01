package S01_ConsoleClasses;

public enum ReportJarConfiguration {
    instance;

    public final String fileSeparator = System.getProperty("file.separator");
    public final String userDirectory = System.getProperty("user.dir");

    public final String nameOfJavaArchive = "report.jar";
    public final String nameOfClass = "Report";


    public final String nameOfSubFolder = "report" + fileSeparator + "jar";
    public final String subFolderPathOfJavaArchive = nameOfSubFolder + fileSeparator + nameOfJavaArchive;
    public final String fullPathToJavaArchive = userDirectory + fileSeparator + subFolderPathOfJavaArchive;

}