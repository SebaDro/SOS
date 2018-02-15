/*
 * Copyright (C) 2018 52°North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.mqtt.decode;

import javax.inject.Inject;
import org.n52.faroe.ConfigurationError;
import org.n52.sos.mqtt.config.MqttConfiguration;
import org.n52.sos.mqtt.convert.AdsbToInsertObservation;
import org.n52.sos.mqtt.convert.FifaInsertObservationConverter;
import org.n52.sos.mqtt.convert.FifaInsertSensorConverter;
import org.n52.sos.mqtt.convert.OmInsertObservationConverter;
import org.n52.sos.mqtt.convert.OmInsertSensorConverter;
import org.n52.sos.mqtt.convert.TtnInsertObservationConverter;
import org.n52.sos.mqtt.convert.TtnInsertSensorConverter;
import org.n52.sos.mqtt.convert.marine.CtdInsertObservationConverter;
import org.n52.sos.mqtt.convert.marine.CtdInsertSensorConverter;
import org.n52.sos.mqtt.convert.marine.FluorometerInsertObservationConverter;
import org.n52.sos.mqtt.convert.marine.FluorometerInsertSensorConverter;
import org.n52.svalbard.encode.EncoderRepository;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MqttDecoderFactory {

    @Inject
    EncoderRepository encoderRepository;

    public static enum Decoder {

        FLUOROMETER {
            public String getName() {
                return "FluorometerDecoder";
            }

            public String qualifiedName() {
                String qualifiedName = String.join("", "org.n52.sos.mqtt.decode.", getName());
                return qualifiedName;
            }
        },
        CTD {
            public String getName() {
                return "CtdDecoder";
            }

            public String qualifiedName() {
                String qualifiedName = String.join("", "org.n52.sos.mqtt.decode.", getName());
                return qualifiedName;
            }
        },
        ADSB {
            public String getName() {
                return "AdsbDecoder";
            }

            public String qualifiedName() {
                String qualifiedName = String.join("", "org.n52.sos.mqtt.decode.", getName());
                return qualifiedName;
            }
        },
        FIFA {
            public String getName() {
                return "FifaDecoder";
            }

            public String qualifiedName() {
                String qualifiedName = String.join("", "org.n52.sos.mqtt.decode.", getName());
                return qualifiedName;
            }
        },
        OM {
            public String getName() {
                return "OmDecoder";
            }

            public String qualifiedName() {
                String qualifiedName = String.join("", "org.n52.sos.mqtt.decode.", getName());
                return qualifiedName;
            }
        },
        TTN {
            public String getName() {
                return "TtnDecoder";
            }

            public String qualifiedName() {
                String qualifiedName = String.join("", "org.n52.sos.mqtt.decode.", getName());
                return qualifiedName;
            }

        };

        public abstract String getName();

        public abstract String qualifiedName();
    }

    public MqttDecoder createMqttDecoder(MqttConfiguration config) {
        MqttDecoder mqttDecoder = null;
        if (config.getDecoder().equals(Decoder.ADSB.qualifiedName())) {
            mqttDecoder = createAdsbDecoder();
        } else if (config.getDecoder().equals(Decoder.CTD.qualifiedName())) {
            mqttDecoder = createCtdDecoder();
        } else if (config.getDecoder().equals(Decoder.FIFA.qualifiedName())) {
            mqttDecoder = createFifaDecoder();
        } else if (config.getDecoder().equals(Decoder.FLUOROMETER.qualifiedName())) {
            mqttDecoder = createFluorometerDecoder();
        } else if (config.getDecoder().equals(Decoder.OM.qualifiedName())) {
            mqttDecoder = createOmDecoder();
        } else if (config.getDecoder().equals(Decoder.TTN.qualifiedName())) {
            mqttDecoder = createTtnDecoder();
        } else {
            throw new ConfigurationError("Could not create MQTT decoder: " + config.getName());
        }
        mqttDecoder.configure(config);
        return mqttDecoder;

    }

    private MqttDecoder createCtdDecoder() {
        return new CtdDecoder()
                .setInsertSensorConverter(new CtdInsertSensorConverter(encoderRepository))
                .setInsertObservationConverter(new CtdInsertObservationConverter());
    }

    private MqttDecoder createAdsbDecoder() {
        return new AdsbDecoder()
                .setInsertSensorConverter(new CtdInsertSensorConverter(encoderRepository))
                .setInsertObservationConverter(new AdsbToInsertObservation());
    }

    private MqttDecoder createFifaDecoder() {
        return new FifaDecoder()
                .setInsertSensorConverter(new FifaInsertSensorConverter(encoderRepository))
                .setInsertObservationConverter(new FifaInsertObservationConverter());
    }

    private MqttDecoder createFluorometerDecoder() {
        return new FluorometerDecoder()
                .setInsertSensorConverter(new FluorometerInsertSensorConverter(encoderRepository))
                .setInsertObservationConverter(new FluorometerInsertObservationConverter());
    }

    private MqttDecoder createOmDecoder() {
        return new OmDecoder()
                .setInsertSensorConverter(new OmInsertSensorConverter(encoderRepository))
                .setInsertObservationConverter(new OmInsertObservationConverter());
    }

    private MqttDecoder createTtnDecoder() {
        return new TtnDecoder()
                .setInsertSensorConverter(new TtnInsertSensorConverter(encoderRepository))
                .setInsertObservationConverter(new TtnInsertObservationConverter());
    }
}
