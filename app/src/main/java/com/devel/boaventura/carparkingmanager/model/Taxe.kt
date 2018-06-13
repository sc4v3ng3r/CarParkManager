package com.devel.boaventura.carparkingmanager.model

import android.os.Parcel
import android.os.Parcelable

data class Taxe(val time: Int, val value: Double) : Parcelable  {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(time)
        parcel.writeDouble(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Taxe> {
        override fun createFromParcel(parcel: Parcel): Taxe {
            return Taxe(parcel)
        }

        override fun newArray(size: Int): Array<Taxe?> {
            return arrayOfNulls(size)
        }
    }

}