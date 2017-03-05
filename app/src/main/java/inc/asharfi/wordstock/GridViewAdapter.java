package inc.asharfi.wordstock;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

@SuppressWarnings("rawtypes")
public class GridViewAdapter extends ArrayAdapter {

	private int layoutResourceId;
	private float width;
	private LayoutInflater rootInflator;
	private ArrayList<Bitmap> data = new ArrayList<Bitmap> ();

	@SuppressWarnings("unchecked")
	public GridViewAdapter(Context context, LayoutInflater inflator, int layoutResourceId, float width, ArrayList<Bitmap> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		this.width = width;
		rootInflator = inflator;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			row = rootInflator.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		//Bitmap bMap = scaleImage(data.get(position));
		Bitmap bMap = data.get(position);
		holder.image.setImageBitmap(bMap);
		return row;
	}
	
	public Bitmap scaleImage(Bitmap bImage) {
		int rectWidth = bImage.getWidth();
		int rectHeight = bImage.getHeight();
		float factor;
		
		if (rectWidth > rectHeight)
    		factor = (float) (width * 1 / 3) / rectWidth;
    	else
    		factor = (float) (width * 1 / 3) / rectHeight;

	    int sizeX = Math.round(rectWidth * factor);
	    int sizeY = Math.round(rectHeight * factor);

	    Bitmap bitmapResized = Bitmap.createScaledBitmap(bImage, sizeX, sizeY, false);

	    return bitmapResized;

	}

	static class ViewHolder {
		ImageView image;
	}
}