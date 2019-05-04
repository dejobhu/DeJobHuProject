/**
 * Copyright 2017 Harish Sridharan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dejobhu.skhu.dejobhu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;


import com.dejobhu.skhu.dejobhu.utils.view.CardPaddingItemDecoration;
import com.dejobhu.skhu.dejobhu.R;
import com.dejobhu.skhu.dejobhu.models.ItemCard;

import java.util.Arrays;
import java.util.List;

public class BaseUtils {

    public static final int TYPE_LIST = 0;


    private static List<ItemCard> getListCards(Resources resources) {
        ItemCard ndtvCard = createItemCard(resources, R.string.ndtv_titletext, R.string.ndtv_image_url,
                R.string.ndtv_subtext, R.string.ndtv_summarytext);

        ItemCard opCard = createItemCard(resources, R.string.op_titletext, R.string.op_image_url,
                R.string.op_subtext, R.string.op_summarytext);

        ItemCard gotCard = createItemCard(resources, R.string.got_titletext, R.string.got_image_url,
                R.string.got_subtext, R.string.got_summarytext);

        ItemCard jetCard = createItemCard(resources, R.string.jet_titletext, R.string.jet_image_url,
                R.string.jet_subtext, R.string.jet_summarytext);

        return Arrays.asList(ndtvCard, opCard, gotCard, jetCard);
    }



    public static List<ItemCard> getCards(Resources resources, int type) {
        List<ItemCard> itemCards;

        switch (type) {

            case TYPE_LIST:
                itemCards = getListCards(resources);
                break;
            default:
                itemCards = null;
        }

        return itemCards;
    }

    public static DemoConfiguration getDemoConfiguration(int configurationType, Context context) {
        DemoConfiguration demoConfiguration;

        switch (configurationType) {
            case TYPE_LIST:
                demoConfiguration = new DemoConfiguration();
                demoConfiguration.setStyleResource(R.style.AppTheme);
                demoConfiguration.setLayoutResource(R.layout.list_expand_activity_list);
                demoConfiguration.setLayoutManager(new LinearLayoutManager(context));
                demoConfiguration.setTitleResource(R.string.ab_list_title);
                break;

            default:
                demoConfiguration = null;
        }

        return demoConfiguration;
    }

    private static ItemCard createItemCard(Resources resources, @StringRes int title, @StringRes int imageUrl,
                                           @StringRes int description, @StringRes int summary) {
        ItemCard itemCard = new ItemCard();

        itemCard.setTitle(resources.getString(title));
        itemCard.setThumbnailUrl(resources.getString(imageUrl));
        itemCard.setDescription(resources.getString(description));
        itemCard.setSummaryText(resources.getString(summary));

        return itemCard;
    }
}
