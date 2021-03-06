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

package eliasstar.jsonrpc.objects.parameter;

import java.io.IOException;
import java.util.Objects;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

/**
 * Represents the params in object format.
 *
 * @author Elias*
 * @since 0.1.0
 * @see <a href="https://www.jsonrpc.org/specification#request_object">JSON-RPC
 *      Specification</a>
 */
public final class ObjectParameters implements Parameters<JsonObject> {

    /** The actual parameters */
    private final JsonObject params;

    /**
     * Creates an {@link ObjectParameters}.
     *
     * @param params The parameters to be used
     */
    public ObjectParameters(JsonObject params) {
        this.params = Objects.requireNonNull(params);
    }

    /**
     * Getter for the actual parameters.
     *
     * @return The actual parameters
     */
    @Override
    public JsonObject get() {
        return params;
    }

    /**
     * Writes a {@link JsonObject} to out.
     *
     * @param out The {@link JsonWriter} used as output
     * @throws IOException If serialization fails
     */
    @Override
    public void write(JsonWriter out) throws IOException {
        Streams.write(params, out);
    }

    /**
     * Returns the hash code for this {@link ObjectParameters}.
     *
     * @return The hash code of the {@link JsonObject}
     */
    @Override
    public int hashCode() {
        return params.hashCode();
    }

    /**
     * Two {@link ObjectParameters} objects are equal if their underlying
     * {@link JsonObject} is equal.
     *
     * @param obj The object to be checked
     * @return {@code true} if the object is equal as described above
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ObjectParameters) {
            var other = (ObjectParameters) obj;

            return this == other || params.equals(other.params);
        }

        return false;
    }

    /**
     * Returns the string representation for this {@link ObjectParameters}.
     *
     * @return The value of the {@link JsonObject} as string
     */
    @Override
    public String toString() {
        return params.toString();
    }

}