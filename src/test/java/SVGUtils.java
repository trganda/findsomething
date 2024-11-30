import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SVGUtils {
    public static BufferedImage renderSVG(InputStream svgInputStream, float width, float height) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 使用 Batik PNGTranscoder 转换 SVG
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_AOI, new Rectangle(0, 0, 24, 24)); // 自动调整区域
        transcoder.addTranscodingHint(ImageTranscoder.KEY_PIXEL_TO_MM, Float.valueOf(0.2645833f));

        TranscoderInput input = new TranscoderInput(svgInputStream);
        TranscoderOutput output = new TranscoderOutput(outputStream);

        transcoder.transcode(input, output);
        outputStream.flush();

        // 读取 PNG 并返回
        return ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
    }
}