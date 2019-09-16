package com.cpd.vos

import android.os.Parcelable
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

/**
 * Created by Hermes Tessaro on 16/10/18.
 */

@Parcelize
class NotificationsVo() : Parcelable {

    var title: String = ""
    var text: String = ""

    constructor(linkedTreeMap: LinkedTreeMap<*, *>) : this() {
        title = linkedTreeMap["title"] as String
        text = linkedTreeMap["text"] as String
    }

    constructor(jsonObject: JSONObject) : this() {
        try {
            title = jsonObject.getString("titulo")
            text = jsonObject.getString("texto")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}