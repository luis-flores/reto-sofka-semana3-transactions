package com.sofka.transactions.services;


import com.sofka.transactions.models.Mongo.M_TransaccionMongo;

public class ErrorGuardado extends RuntimeException {
    private M_TransaccionMongo transaccion;

    public ErrorGuardado(String message, M_TransaccionMongo transaccion) {
        super(message);

        this.transaccion = transaccion;
    }

    public M_TransaccionMongo getTransaccion() {
        return transaccion;
    }
}
