package xiuery.xselenium.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@ComponentScan
public class FileResource {

    @Value("${x-api.je.root-path}")
    private String root_path;

    public void fileWrite(String path, String filename, String str) {
        File file = new File(root_path + "/" + path +"/" + filename);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        if (! fileSystemResource.exists()) {
            // 创建文件夹
            if (! createPath(root_path + "/" + path)) {
                return;
            }
        }

        // 写内容
        try {
            FileWriter fileWriter = (new FileWriter(fileSystemResource.getFile()));
            fileWriter.write(str);
            fileWriter.close();
        } catch (IOException e) {
            //todo
        }
    }

    private boolean createPath(String path) {
        File file = new File(path);
        // 递归创建
        if (! file.exists()) {
            if (file.mkdirs()) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }
}
