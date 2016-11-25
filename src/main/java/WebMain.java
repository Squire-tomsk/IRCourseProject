/**
 * Created by abuca on 18.11.16.
 */

import DAO.BasicDAO;
import engine.SimpleSearchEngine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static spark.Spark.*;

public class WebMain {
    public static void main(String[] args) throws IOException {
        BasicDAO.init();
        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        WebMain obj = new WebMain();
        String webForm = obj.getFile("main_page.html");
        post("/", (req, res) -> {
            SimpleSearchEngine searchEngine = new SimpleSearchEngine();
            String query = req.queryParams("query");
            if (query == null || query.isEmpty()){
                return webForm;
            }
            else {
                List<String> result = searchEngine.getDocumentsOrLinks(query);
                String resultString = result.stream().reduce((str1,str2) -> str1+"<br><hr><br>"+str2).toString();
                return webForm.replaceFirst("<result>",resultString);
            }
        });

        get("/", (req, res) -> webForm);
    }

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line);
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
}