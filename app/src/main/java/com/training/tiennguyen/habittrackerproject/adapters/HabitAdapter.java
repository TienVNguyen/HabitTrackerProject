/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.habittrackerproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.training.tiennguyen.habittrackerproject.activites.DetailsActivity;
import com.training.tiennguyen.habittrackerproject.activites.R;
import com.training.tiennguyen.habittrackerproject.constants.VariableConstant;
import com.training.tiennguyen.habittrackerproject.database.HabitDbHelper;
import com.training.tiennguyen.habittrackerproject.models.HabitModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * HabitAdapter
 *
 * @author TienNguyen
 */
public class HabitAdapter extends ArrayAdapter<HabitModel> {
    /**
     * mResource
     */
    private int mResource;

    /**
     * Constructor
     *
     * @param context  Context
     * @param resource int
     * @param objects  List<HabitModel>
     */
    public HabitAdapter(Context context, int resource, List<HabitModel> objects) {
        super(context, 0, objects);
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Caching
        final HabitViewHolder holder;
        if (convertView != null) {
            holder = (HabitViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            holder = new HabitViewHolder(convertView);
            convertView.setTag(holder);
        }

        // Populating
        final HabitModel model = getItem(position);
        if (model != null) {
            holder.txtTitle.setText(model.getmTitle());
            holder.txtDate.setText(model.getmDate());
            holder.cbCompleted.setChecked(model.getmCompleted() != 0);
            holder.cbCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setmCompleted(holder.cbCompleted.isChecked() ? 1 : 0);

                    HabitDbHelper habitDbHelper = new HabitDbHelper(getContext());
                    int result = habitDbHelper.updateHabit(model);
                    if (result > 0) {
                        Toast.makeText(getContext(), "SUCCESS" + model.getmTitle(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "FAILED" + model.getmTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(VariableConstant.HABIT_PARCELABLE, model);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * HabitViewHolder
     */
    protected static class HabitViewHolder {
        @BindView(R.id.lvHabits_txtTitle)
        protected TextView txtTitle;
        @BindView(R.id.lvHabits_txtDate)
        protected TextView txtDate;
        @BindView(R.id.lvHabits_cbCompleted)
        protected CheckBox cbCompleted;

        /**
         * Constructor
         *
         * @param view View
         */
        protected HabitViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
