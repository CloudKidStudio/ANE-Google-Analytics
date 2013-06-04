/*
 * Air Native Extension for Google Analytics on iOS and Android
 * 
 * Author: Alessandro Bianco
 * http://alessandrobianco.eu
 *
 * Copyright (c) 2012 Alessandro Bianco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
 package eu.alebianco.air.extensions.analytics.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import eu.alebianco.air.extensions.analytics.GAContext;
import eu.alebianco.air.extensions.utils.FREUtils;
import eu.alebianco.air.extensions.utils.LogLevel;

public class SetCustomVar implements FREFunction {

	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		
		if (args == null || args.length < 3) {
			FREUtils.logEvent(context, LogLevel.FATAL, "Invalid arguments number for method '%s'.", FREUtils.getClassName());
			return FREUtils.createRuntimeException("ArgumentError", 0, "Invalid number of arguments sent to the method '%s'.", FREUtils.getClassName());
		}
		
		FREObject result = null;
		
		int index;
		String name;
		String value;
		int scope = 3;

        try {
        	index = Math.max(1, Math.min(5, args[0].getAsInt()));
        } catch (Exception e) {
            FREUtils.logEvent(context, LogLevel.FATAL, "Unable to read the 'index' parameter.\n(Exception:[name:%s,reason:%s,method:%s])", 
            		FREUtils.stripPackageFromClassName(e.toString()), e.getMessage(), FREUtils.getClassName());
            return FREUtils.createRuntimeException("ArgumentError", 0, "Unable to read the 'index' parameter on method '%s'.", FREUtils.getClassName());
        }

        try {
        	name = args[1].getAsString();
        } catch (Exception e) {
            FREUtils.logEvent(context, LogLevel.FATAL, "Unable to read the 'name' parameter.\n(Exception:[name:%s,reason:%s,method:%s])", 
            		FREUtils.stripPackageFromClassName(e.toString()), e.getMessage(), FREUtils.getClassName());
            return FREUtils.createRuntimeException("ArgumentError", 0, "Unable to read the 'name' parameter on method '%s'.", FREUtils.getClassName());
        }
        
        try {
        	value = args[2].getAsString();
        } catch (Exception e) {
        	FREUtils.logEvent(context, LogLevel.FATAL, "Unable to read the 'value' parameter.\n(Exception:[name:%s,reason:%s,method:%s])", 
        			FREUtils.stripPackageFromClassName(e.toString()), e.getMessage(), FREUtils.getClassName());
        	return FREUtils.createRuntimeException("ArgumentError", 0, "Unable to read the 'value' parameter on method '%s'.", FREUtils.getClassName());
        }
        
        if (args.length >= 4 && args[3] != null) {
	        try {
	        	scope = (args.length >= 4) ? Math.max(1, Math.min(3, args[3].getAsInt())) : 3;
	        } catch (Exception e) {
                FREUtils.logEvent(context, LogLevel.WARN, "Unable to read the 'scope' parameter.\n(Exception:[name:%s,reason:%s,method:%s])", 
                		FREUtils.stripPackageFromClassName(e.toString()), e.getMessage(), FREUtils.getClassName());
	        }
        }
        
		GoogleAnalyticsTracker tracker = ((GAContext) context).tracker;
		Boolean success = tracker.setCustomVar(index, name, value, scope);
			
		try {
			result = FREObject.newObject(success);
		} catch(Exception e) {
			FREUtils.logEvent(context, LogLevel.ERROR, "Unable to create the return value.\n(Exception:[name:%s,reason:%s,method:%s])", 
					FREUtils.stripPackageFromClassName(e.toString()), e.getMessage(), FREUtils.getClassName());
		}
		
		return result;
	}

}