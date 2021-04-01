import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Test {
    public static boolean isImage(File file) {
        ImageInputStream iis = null;

        try {
            iis = ImageIO.createImageInputStream(file);
        } catch (IOException var14) {
            var14.printStackTrace();
            //logger.error("上传文件错误");
            return false;
        }

        Iterator iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            //logger.error("上传文件：" + file.getName() + " 不是图片文件");
            return false;
        } else {
            ImageReader reader = (ImageReader)iter.next();

            try {
                System.out.println("上传图片格式为:" + reader.getFormatName());
                return true;
            } catch (IOException var15) {
                var15.printStackTrace();
                //logger.info("获取图片后缀失败");
            } finally {
                try {
                    iis.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }

            }

            return false;
        }
    }


    public static void main(String[] args) {
        System.out.println(isImage(new File("/Users/c0ny1/Documents/codebak/ysoserial/src/test/java/aaaa.txt")));
    }
}
