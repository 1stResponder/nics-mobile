/*|~^~|Copyright (c) 2008-2016, Massachusetts Institute of Technology (MIT)
 |~^~|All rights reserved.
 |~^~|
 |~^~|Redistribution and use in source and binary forms, with or without
 |~^~|modification, are permitted provided that the following conditions are met:
 |~^~|
 |~^~|-1. Redistributions of source code must retain the above copyright notice, this
 |~^~|ist of conditions and the following disclaimer.
 |~^~|
 |~^~|-2. Redistributions in binary form must reproduce the above copyright notice,
 |~^~|this list of conditions and the following disclaimer in the documentation
 |~^~|and/or other materials provided with the distribution.
 |~^~|
 |~^~|-3. Neither the name of the copyright holder nor the names of its contributors
 |~^~|may be used to endorse or promote products derived from this software without
 |~^~|specific prior written permission.
 |~^~|
 |~^~|THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 |~^~|AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 |~^~|IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 |~^~|DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 |~^~|FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 |~^~|DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 |~^~|SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 |~^~|CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 |~^~|OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 |~^~|OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\*/
/**
 *
 */
package edu.mit.ll.nics.android.api.tasks;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import edu.mit.ll.nics.android.api.DataManager;
import edu.mit.ll.nics.android.api.RestClient;
import edu.mit.ll.nics.android.api.data.ReportSendStatus;
import edu.mit.ll.nics.android.api.payload.forms.FieldReportPayload;
import edu.mit.ll.nics.android.utils.Intents;
import edu.mit.ll.nics.android.utils.NotificationsHandler;


public class ParseFieldReportsTask extends AsyncTask<ArrayList<FieldReportPayload>, Object, Integer> {
	private Context mContext;
	private DataManager mDataManager;
	private NotificationsHandler mNotificationHandler;
	
	public ParseFieldReportsTask(Context context) {
		mContext = context;
		mDataManager = DataManager.getInstance(mContext);
		mNotificationHandler = NotificationsHandler.getInstance(mContext);
	}

	@Override
	protected Integer doInBackground(@SuppressWarnings("unchecked") ArrayList<FieldReportPayload>... frPayloads) {
		Integer numParsed = 0;
		for(FieldReportPayload payload : frPayloads[0]) {
			if(payload.getIncidentId() == mDataManager.getActiveIncidentId()) {
				payload.parse();
				payload.setSendStatus(ReportSendStatus.SENT);
				payload.setNew(true);
				
				mDataManager.addFieldReportToHistory(payload);
				
		        Intent intent = new Intent();
		        intent.setAction(Intents.nics_NEW_FIELD_REPORT_RECEIVED);
		        intent.putExtra("payload", payload.toJsonString());
		        intent.putExtra("sendStatus", ReportSendStatus.SENT.getId());
		        mContext.sendBroadcast (intent);
		        numParsed++;
			}
		}
		
		if(numParsed > 0) {
			
			ArrayList<FieldReportPayload> reports = mDataManager.getAllFieldReportStoreAndForwardHasSent();
			for(int i = 0; i < reports.size(); i++){
				mDataManager.deleteFieldReportStoreAndForward(reports.get(i).getId());
				Log.d("ParseFieldReport","deleted sent field report: " + reports.get(i).getId());
				Intent intent = new Intent();
			    intent.setAction(Intents.nics_SENT_FIELD_REPORTS_CLEARED);
				intent.putExtra("reportId", reports.get(i).getFormId());
		        mContext.sendBroadcast (intent);
			}
			
			if(!mDataManager.isPushNotificationsDisabled()){
				mNotificationHandler.createFieldReportNotification(frPayloads[0], mDataManager.getActiveIncidentId());
			}
	        mDataManager.addPersonalHistory("Successfully received " + numParsed + " field reports from " + mDataManager.getActiveIncidentName());
		}
		return numParsed;
	}
	
	@Override
	protected void onPostExecute(Integer numParsed) {
		super.onPostExecute(numParsed);
		
		RestClient.clearParseFieldReportTask();
		Log.i("nicsFieldReportTask", "Successfully parsed " + numParsed + " field reports.");
	}

}
