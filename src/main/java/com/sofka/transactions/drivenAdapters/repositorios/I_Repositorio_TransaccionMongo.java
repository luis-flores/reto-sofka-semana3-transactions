package com.sofka.transactions.drivenAdapters.repositorios;

import com.sofka.transactions.models.Mongo.M_TransaccionMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface I_Repositorio_TransaccionMongo extends ReactiveMongoRepository<M_TransaccionMongo, String>
{
}
