/*
 * Copyright (C) 2020-2021 Elias*
 *
 * This file is part of JsonRpc4J.
 *
 * JsonRpc4J is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or any later version.
 *
 * JsonRpc4J is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JsonRpc4J. If not, see <https://www.gnu.org/licenses/>.
 */

package eliasstar.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Gson {@link TypeAdapter} for {@link Optional}.
 * <p>
 * Serializes value if present or else omits field entirely.
 *
 * @author Elias*
 * @since 0.1.0
 */
final class OptionalTypeAdapter extends TypeAdapter<Optional<?>> {

    /** The {@link Gson} instance to which this adapter is registered. */
    private final Gson gson;

    /** The {@link Type} of the optionals value. */
    private final Type typeOfOptional;

    /**
     * Used by {@link OptionalTypeAdapterFactory}.
     *
     * @param gson The current gson instance
     * @param typeOfOptional The type parameter of the {@link Optional}
     */
    OptionalTypeAdapter(Gson gson, Type typeOfOptional) {
        this.gson = gson;
        this.typeOfOptional = typeOfOptional;
    }

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null) for
     * the value of optional.
     *
     * @param out The {@link JsonWriter} used as output
     * @param optional The {@link Optional} which is serialized
     * @throws IOException If serialization fails
     */
    @Override
    public void write(JsonWriter out, Optional<?> optional) throws IOException {
        var serialize = out.getSerializeNulls();

        var json = optional.map(x -> gson.toJsonTree(x));

        if (json.isPresent()) {
            out.setSerializeNulls(true);
            Streams.write(json.get(), out);
        } else {
            out.setSerializeNulls(false);
            out.nullValue();
        }

        out.setSerializeNulls(serialize);
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null) and
     * converts it to an {@link Optional} containing the value.
     *
     * @param in The {@link JsonReader} used as input
     * @return An {@link Optional} containing the read value
     * @throws IOException If deserialization fails
     */
    @Override
    public Optional<?> read(JsonReader in) throws IOException {
        return Optional.ofNullable(gson.fromJson(in, typeOfOptional));
    }

}