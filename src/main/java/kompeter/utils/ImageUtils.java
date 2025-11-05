/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUtils {
    public static final Path copyMove(final String sourcePath, final String targetPath) throws IOException {
        final Path s = Paths.get(sourcePath);
        final Path t = Paths.get(targetPath);

        final Path res = Files.copy(s, t.resolve(s.getFileName()), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Created " + res.toString());

        return res;
    }
}
