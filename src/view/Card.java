package view;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.bandeco.R;
import com.fima.cardsui.objects.RecyclableCard;

public class Card extends RecyclableCard {

	public Card(String titlePlay, String description, String color,
			String titleColor, Boolean isClickable) {
		super(titlePlay, description, color, titleColor, false,
				isClickable);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(titlePlay);
		((TextView) convertView.findViewById(R.id.title)).setTextColor(Color
				.parseColor(titleColor));
		((TextView) convertView.findViewById(R.id.description))
				.setText(description);

		if (isClickable == true)
			((LinearLayout) convertView.findViewById(R.id.contentLayout))
					.setBackgroundResource(R.drawable.selectable_background_cardbank);

	}
}
