/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aaf.com.br.favodemelapp.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aaf.com.br.favodemelapp.Util.JsonReader;

public class Service {

	public static Object getObject(String endPoint){
		Object obj = null;
		try{
			JSONObject jo = aaf.com.br.favodemelapp.Util.JsonReader.getObject(endPoint);
			obj =com.cedarsoftware.util.io.JsonReader.jsonToJava(jo.toString());
			return obj;
		}catch (Exception e){
		//	e.printStackTrace();
		}
		return obj;
	}

	public static Object getObject(JSONObject endPoint){
		return com.cedarsoftware.util.io.JsonReader.jsonToJava(endPoint.toString());
	}

	public static Object getObjectList(String endPoint){
 		List<Object> objcs = new ArrayList<>();
		JSONArray jsonArray = JsonReader.getList(endPoint);
		try {
			for(int i=0; i<jsonArray.length();i++){

				objcs.add(jsonArray.getJSONObject(i));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return objcs;
	}

}
