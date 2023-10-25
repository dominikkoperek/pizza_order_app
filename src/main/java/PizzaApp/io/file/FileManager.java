package PizzaApp.io.file;

import PizzaApp.exception.ImportException;
import PizzaApp.Model.LoginModel.DataBase;

import java.io.*;

/**
 * A class responsible for importing and exporting data to/from a database file.
 */
public class FileManager  {
    private static final String FILE_NAME = "accounts.obj";

    /**
     * Imports data from the specified file and returns it as a DataBase object.
     *
     * @return A DataBase object containing the imported data.
     * @throws ImportException if the file is not found or there's an issue importing the data.
     */

    public DataBase importData() {
        try (
                var fis = new FileInputStream(FILE_NAME);
                var ois = new ObjectInputStream(fis);
        )
        {
            return (DataBase) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new ImportException("Can't find file " + FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            throw  new ImportException("Can't import file " +FILE_NAME);
        }
    }

    /**
     * Exports the provided DataBase object to a file.
     *
     * @param dataBase The DataBase object to be exported.
     */
    public void exportData(DataBase dataBase) {
        try (
                var fos = new  FileOutputStream (FILE_NAME);
                var oos = new ObjectOutputStream(fos);
        )
        {
            oos.writeObject(dataBase);
            System.out.println("File saved!");
        }catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
