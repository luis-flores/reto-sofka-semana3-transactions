package com.sofka.transactions.drivenAdapters.repositorios;

import com.sofka.transactions.models.Mongo.M_CuentaMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface I_RepositorioCuentaMongo extends ReactiveMongoRepository<M_CuentaMongo, String>
{
}
