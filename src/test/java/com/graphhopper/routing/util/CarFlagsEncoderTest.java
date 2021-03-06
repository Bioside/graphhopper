/*
 *  Licensed to Peter Karich under one or more contributor license 
 *  agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  Peter Karich licenses this file to you under the Apache License, 
 *  Version 2.0 (the "License"); you may not use this file except 
 *  in compliance with the License. You may obtain a copy of the 
 *  License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing.util;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Peter Karich
 */
public class CarFlagsEncoderTest {

    private CarFlagEncoder encoder = new CarFlagEncoder();

    @Test
    public void testBasics() {
        assertTrue(encoder.isForward(encoder.flagsDefault(true)));
        assertTrue(encoder.isBackward(encoder.flagsDefault(true)));
        assertTrue(encoder.isBoth(encoder.flagsDefault(true)));

        assertTrue(encoder.isForward(encoder.flagsDefault(false)));
        assertFalse(encoder.isBackward(encoder.flagsDefault(false)));
        assertFalse(encoder.isBoth(encoder.flagsDefault(false)));
    }

    @Test
    public void testOverwrite() {
        int forward = encoder.flags(10, false);
        int backward = encoder.swapDirection(forward);
        int both = encoder.flags(20, true);
        assertTrue(encoder.canBeOverwritten(forward, forward));
        assertTrue(encoder.canBeOverwritten(backward, backward));
        assertTrue(encoder.canBeOverwritten(forward, both));
        assertTrue(encoder.canBeOverwritten(backward, both));

        assertTrue(encoder.canBeOverwritten(both, both));
        assertFalse(encoder.canBeOverwritten(both, forward));
        assertFalse(encoder.canBeOverwritten(both, backward));
        assertFalse(encoder.canBeOverwritten(forward, backward));
        assertFalse(encoder.canBeOverwritten(backward, forward));
    }

    @Test
    public void testSwapDir() {
        int swappedFlags = encoder.swapDirection(encoder.flagsDefault(true));
        assertTrue(encoder.isForward(swappedFlags));
        assertTrue(encoder.isBackward(swappedFlags));

        swappedFlags = encoder.swapDirection(encoder.flagsDefault(false));

        assertFalse(encoder.isForward(swappedFlags));
        assertTrue(encoder.isBackward(swappedFlags));
        
        assertEquals(0, encoder.swapDirection(0));
    }

    @Test
    public void testService() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("car", encoder.getSpeed("service"));
        int flags = new AcceptWay(true, false, false).toFlags(properties);
        assertTrue(encoder.isForward(flags));
        assertTrue(encoder.isBackward(flags));
        assertTrue(encoder.isService(flags));
    }
}