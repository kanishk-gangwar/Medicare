package com.example.login

class medicines{
    var medname : String? = null
    var description : String? = null
    var symptoms : String? = null

    constructor(){

    }
    constructor( medname: String?, description: String?, symptoms: String?) {
        this.medname = medname
        this.description = description
        this.symptoms = symptoms
    }
}