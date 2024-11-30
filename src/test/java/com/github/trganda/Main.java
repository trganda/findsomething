package com.github.trganda;

import com.aspose.html.dom.svg.SVGDocument;
import com.aspose.html.saving.ImageSaveOptions;

public class Main {
    public static void main(String[] args) {
        // Prepare a path to a source SVG file
        String documentPath = Main.class.getClassLoader().getResource("filter.svg").toString();

        // Prepare a path for converted file saving
        String savePath = "gradient-options.png";

        // Initialize an SVG document from the file
        SVGDocument document = new SVGDocument(documentPath);
        try {
            // Initialize an instance of ImageSaveOptions
            ImageSaveOptions options = new ImageSaveOptions();

            // Convert SVG to PNG
            com.aspose.html.converters.Converter.convertSVG(document, options, savePath);
        }
        finally {
            if (document != null)
                document.dispose();
        }
    }
}
