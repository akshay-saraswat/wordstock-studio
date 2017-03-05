package inc.asharfi.wordstock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundRectImageView extends ImageView
{
    private int borderWidth = 4;
    private int viewWidth;
    private int viewHeight;
    private Bitmap image;
    private Paint paint;
    private Paint paintBorder;

    public RoundRectImageView(Context context)
    {
        super(context);
        setup();
    }

    public RoundRectImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setup();
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup()
    {
        // init paint
        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        setBorderColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        paintBorder.setShadowLayer(3.0f, 2.0f, 2.0f, Color.BLACK);
    }

    public void setBorderWidth(int borderWidth)
    {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    public void setBorderColor(int borderColor)
    {
        if (paintBorder != null)
            paintBorder.setColor(borderColor);

        this.invalidate();
    }

    private void loadBitmap()
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

        if (bitmapDrawable != null)
            image = bitmapDrawable.getBitmap();
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas)
    {
        // load the bitmap
        loadBitmap();

        // init shader
        if (image != null)
        {
        	int imageWidth, imageHeight;
        	float left, top;
            final Rect innerRect = new Rect((int)(0 + borderWidth + 1.0f), (int)(0 + borderWidth + 1.0f), (int)(viewWidth + borderWidth - 3.0f), (int)(viewHeight + borderWidth - 3.0f));
            final RectF innerRectF = new RectF(innerRect);
            final Rect outerRect = new Rect((int)(0 + 1.0f), (int)(0 + 1.0f), (int)(viewWidth + (2*borderWidth) - 3.0f), (int)(viewHeight + (2*borderWidth) - 3.0f));
            final RectF outerRectF = new RectF(outerRect);
            final float roundPx = 30;

            canvas.drawRoundRect(outerRectF, roundPx, roundPx, paintBorder);
            canvas.drawRoundRect(innerRectF, roundPx, roundPx, paint);

        	if (image.getWidth() > image.getHeight()) {
        		imageWidth = viewWidth - 4;
        		imageHeight = (image.getHeight() * imageWidth / image.getWidth());
        		left = 0 + borderWidth + 1;
        		top = ((viewHeight - imageHeight + 1) / 2) + borderWidth;
        	} else {
        		imageHeight = viewHeight - 4;
        		imageWidth = (image.getWidth() * imageHeight / image.getHeight());
        		top = 0 + borderWidth + 1;
        		left = ((viewWidth - imageWidth + 1) / 2) + borderWidth;
        	}

        	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //canvas.drawBitmap(Bitmap.createScaledBitmap(image, imageWidth, imageHeight, false), innerRect, innerRect, paint);
            canvas.drawBitmap(Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true), left, top, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth = width - (borderWidth * 2);
        viewHeight = height - (borderWidth * 2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text
            result = viewWidth;
        }

        return result;
    }

    private int measureHeight(int measureSpecHeight, int measureSpecWidth)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }

        return (result + 2);
    }
}
