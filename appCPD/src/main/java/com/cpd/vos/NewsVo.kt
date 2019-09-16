/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.vos

import android.os.Parcelable
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

/**
 * News value object.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */

@Parcelize
class NewsVo() : Parcelable {

    var title: String = ""
    var shortText: String = ""
    var date: String = ""
    var imgThumbUrl: String = ""
    var imgLargeUrl: String = ""
    var text: String = ""
    var imageUrl: String? = null
    var caption: String? = null

    constructor(linkedTreeMap: LinkedTreeMap<*, *>) : this() {
        title = linkedTreeMap["title"] as String
        shortText = linkedTreeMap["shortText"] as String
        date = linkedTreeMap["date"] as String
        imgLargeUrl = linkedTreeMap["imgLargeUrl"] as String
        imgThumbUrl = linkedTreeMap["imgThumbUrl"] as String
        text = linkedTreeMap["text"] as String
        imageUrl = linkedTreeMap["imageUrl"] as String
        caption = linkedTreeMap["caption"] as String
    }

    constructor(jsonObject: JSONObject) : this() {
        try {
            title = jsonObject.getString("titulo")
            shortText = jsonObject.getString("chamada")
            date = jsonObject.getString("data")
            imgLargeUrl = jsonObject.getString("image_large")
            imgThumbUrl = jsonObject.getString("image_thumb")
            text = jsonObject.getString("texto")

            try {
                val img = jsonObject.getJSONObject("imagem")
                imageUrl = img.getString("url")
                caption = img.getString("legenda")
            } catch (e1: Exception) {
                e1.printStackTrace()
                imageUrl = null
                caption = null
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
