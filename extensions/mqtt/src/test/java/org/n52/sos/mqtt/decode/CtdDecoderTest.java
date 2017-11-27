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
package org.n52.sos.mqtt.decode;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.mqtt.api.CtdMessage;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author Sebastian Drost
 */
public class CtdDecoderTest {
    
    @Test
    public void parseMessageTest(){
        String observationPayload  = "2017-11-16T12:00:55.517Z|I-OCEAN7-304-0616641|  24.07  12.625  37.067  31.615 1495.3035 18:54:31.20M";
        String paramPayload = "2017-11-16T12:00:50.468Z|I-OCEAN7-304-0616641|  Press   Temp    Cond    Sal    SoundV";
        String acquisitionPayload  = "2017-11-16T12:00:50.468Z|I-OCEAN7-304-0616641|Acquisition: <^C>Stop";
        
        CtdDecoder decoder = new CtdDecoder();
        decoder.setLineSeperator("\n");
        MqttMessage observationMessage =  decoder.parseMessage(observationPayload);
        MqttMessage paramMessage = decoder.parseMessage(paramPayload);
        CtdMessage acquisitionMessage = (CtdMessage) decoder.parseMessage(acquisitionPayload);
        
        Assert.assertThat(((CtdMessage)observationMessage).getPressure(), CoreMatchers.equalTo(24.07));
        Assert.assertThat(((CtdMessage)observationMessage).getTemperature(), CoreMatchers.equalTo(12.625));
        Assert.assertThat(((CtdMessage)observationMessage).getConductivity(), CoreMatchers.equalTo(37.067));
        Assert.assertThat(((CtdMessage)observationMessage).getSalinity(), CoreMatchers.equalTo(31.615));
        Assert.assertThat(((CtdMessage)observationMessage).getSoundVelocity(), CoreMatchers.equalTo(1495.3035));
        
        Assert.assertThat(paramMessage, CoreMatchers.nullValue());
        Assert.assertThat(acquisitionMessage, CoreMatchers.nullValue());
    }
       
}
