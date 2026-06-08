package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This box may be present in any visual sample entry. This box indicates the
 * scaling method that is applied when the width and height of the visual
 * material (as declared by the width and height values in any visual sample
 * entry) do not match the track width and height values (as indicated in the
 * track header box).
 * Implementation of this box is optional; if this box is present and can be
 * interpreted by the decoder, all samples shall be displayed according to the
 * scaling behaviour that is specified in this box. Otherwise, all samples are
 * scaled to the size that is indicated by the width and height field in the
 * Track Header Box.
 * If the size of the image is bigger than the size of the presentation region
 * and 'hidden' scaling is applied in the Sample Scale Box, it is not possible
 * to display the whole image. In such a case, it is useful to provide the
 * information to determine the region that is to be displayed. The centre
 * values would then indicate the centre of the region of high priority in each
 * visual sample. The decoder can display the region of high priority according
 * to these values. The centre values imply a consistent crop for all the images
 * in a sequence. The offset values are positive when the desired visual centre
 * is below or to the right of the image centre, and negative for offsets above
 * or to the left.
 *
 * @author in-somnia
 */
public class SampleScaleBox extends FullBox {

    private boolean constrained;
    private int scaleMethod, displayCenterX, displayCenterY;

    public SampleScaleBox() {
        super("Sample Scale Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        //7 bits reserved, 1 bit flag
        constrained = (in.read() & 1) == 1;

        scaleMethod = in.read();
        displayCenterX = (int) in.readBytes(2);
        displayCenterY = (int) in.readBytes(2);
    }

    /**
     * If this flag is set, all samples described by this sample entry shall be
     * scaled according to the method specified by the field 'scale_method'.
     * Otherwise, it is recommended that all the samples be scaled according to
     * the method specified by the field 'scale_method', but can be displayed in
     * an implementation dependent way, which may include not scaling the image
     * (i.e. neither to the width and height specified in the track header box,
     * nor by the method indicated here).
     *
     * @return true if the samples should be scaled by the scale method
     */
    public boolean isConstrained() {
        return constrained;
    }

    /**
     * The horizontal offset in pixels of the centre of the region that should
     * be displayed by priority relative to the centre of the image. Default
     * value is zero. Positive values indicate a display centre to the right of
     * the image centre.
     *
     * @return the horizontal offset
     */
    public int getDisplayCenterX() {
        return displayCenterX;
    }

    /**
     * The vertical offset in pixels of the centre of the region that should be
     * displayed by priority relative to the centre of the image. Default value
     * is zero. Positive values indicate a display centre below the image
     * centre.
     *
     * @return the vertical offset
     */
    public int getDisplayCenterY() {
        return displayCenterY;
    }

    /**
     * The scale method is an integer that defines the scaling mode to be used.
     * Of the 256 possible values the values 0 through 127 are reserved for use
     * by ISO and values 128 through 255 are user-defined and are not specified
     * in this International Standard; they may be used as determined by the
     * application. Of the reserved values the following modes are currently
     * defined:
     * 1: scaling is done by 'fill' mode.
     * 2: scaling is done by 'hidden' mode.
     * 3: scaling is done by 'meet' mode.
     * 4: scaling is done by 'slice' mode in the x-coordinate.
     * 5: scaling is done by 'slice' mode in the y-coordinate.
     *
     * @return the scale method
     */
    public int getScaleMethod() {
        return scaleMethod;
    }
}
