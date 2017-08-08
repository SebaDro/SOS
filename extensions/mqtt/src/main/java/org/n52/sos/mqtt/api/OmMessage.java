/*
 * Copyright (C) 2017 52north.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.n52.sos.mqtt.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author Sebastian Drost
 */
public class OmMessage implements MqttMessage {

    public static final String PROCEDURE = "procedure";

    private JsonNode omPayload;
    private String procedure;

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    @Override
    public String getProcedure() {
        return procedure;
    }
    
    public OmMessage setOmPayload(JsonNode omPayload){
        this.omPayload = omPayload;
        return this;
    }

    public JsonNode getOmPayload() {
        return omPayload;
    }
    
    

}
