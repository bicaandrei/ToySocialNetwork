package Repository;

import Domain.Entity;
import Domain.Validators.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator){

        super(validator);
        this.fileName = fileName;

    }

    public void loadData(){

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
            this.clearRepository();
            String newLine;
            try {
                while((newLine = reader.readLine()) != null) {
                    //System.out.println(newLine);
                    List<String> data = Arrays.asList(newLine.split(";"));
                    E entity = this.extractEntity(data);
                    super.save(entity);
                }
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            reader.close();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    public abstract E extractEntity(List<String> var1);

    protected abstract String createEntityAsString(E var1);

    public Optional<E> save(E entity) {
        Optional<E> result = super.save(entity);
        if (result == null) {
            this.writeToFile(entity);
        }

        return result;
    }
    public void writeToFile(E entity) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName, true));

            try {
                writer.write(this.createEntityAsString(entity));
                writer.newLine();
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            writer.close();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }
    public void clearFile(){

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName));

            writer.write("");

            writer.close();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

}
