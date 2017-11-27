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
import org.n52.sos.mqtt.api.FluorometerMessage;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author Sebastian
 */
public class FluorometerDecoderTest {
    
        @Test
    public void parseMessageTest(){
        String observationPayload  = "2017-11-23T09:36:27.375Z|WL-ECO-FLNTU-4476|11/23/17	09:34:42	695	56	700	212	547";
        
        FluorometerDecoder decoder = new FluorometerDecoder();
        decoder.setLineSeperator("\n");
        MqttMessage observationMessage =  decoder.parseMessage(observationPayload);
        
        Assert.assertThat(((FluorometerMessage)observationMessage).getFluorescenceWavelength(), CoreMatchers.equalTo(695.));
        Assert.assertThat(((FluorometerMessage)observationMessage).getChlCount(), CoreMatchers.equalTo(56));
        Assert.assertThat(((FluorometerMessage)observationMessage).getTurbidityWavelength(), CoreMatchers.equalTo(700.));
        Assert.assertThat(((FluorometerMessage)observationMessage).getNtuCount(), CoreMatchers.equalTo(212));
        Assert.assertThat(((FluorometerMessage)observationMessage).getThermistor(), CoreMatchers.equalTo(547));
    }
    
}
