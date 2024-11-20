package com.example.studentregister.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("logradouro") val street: String?,
    @SerializedName("complemento") val complement: String?,
    @SerializedName("bairro") val neighborhood: String?,
    @SerializedName("localidade") val city: String?,
    @SerializedName("uf") val state: String?
)
