package xiuery.xselenium.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileResource {

    @Value("${x-config.je.code-path}")
    private String codePath;

    public boolean fileWrite(String path, String filename, String str) {
        File file = new File(codePath + "/" + path +"/" + filename);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        if (! fileSystemResource.exists()) {
            // 创建文件夹
            if (! createPath(codePath + "/" + path)) {
                return false;
            }
        }

        // 写内容
        try {
            FileWriter fileWriter = (new FileWriter(fileSystemResource.getFile()));
            fileWriter.write(str);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
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
