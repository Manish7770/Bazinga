package com.manish.bazingalnmiit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.manish.bazingalnmiit.Commondata.Commondata;

public class TandC extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tandc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.abouttoolbar);
        toolbar.setTitle("Terms and Conditions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView=(TextView)findViewById(R.id.contacttext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<h2><b>TERMS AND CONDITIONS</h2></b><br><br><p>The terms &quot;We&quot; / &quot;Us&quot; / &quot;Our&quot;/”Company” individually and collectively refer to KRV Hospitalities and the\n" +
                    "terms &quot;Visitor” ”User” refer to the users.<br><br>\n" +
                    "This page states the Terms and Conditions under which you (Visitor) may visit this Application\n" +
                    "(“Bazinga”). Please read this page carefully. If you do not accept the Terms and Conditions stated here,\n" +
                    "we would request you to exit this site. The business, any of its business divisions and / or its subsidiaries,\n" +
                    "associate companies or subsidiaries to subsidiaries or such other investment companies (in India or\n" +
                    "abroad) reserve their respective rights to revise these Terms and Conditions at any time by updating this\n" +
                    "posting. You should visit this page periodically to re-appraise yourself of the Terms and Conditions,\n" +
                    "because they are binding on all users of this Website.<br><br>\n" +
                    "<b>USE OF CONTENT</b><br><br>\n" +
                    "All logos, brands, marks headings, labels, names, signatures, numerals, shapes or any combinations\n" +
                    "thereof, appearing in this site, except as otherwise noted, are properties either owned, or used under\n" +
                    "licence, by the business and / or its associate entities who feature on this Website. The use of these\n" +
                    "properties or any other content on this site, except as provided in these terms and conditions or in the site\n" +
                    "content, is strictly prohibited.<br><br>\n" +
                    "You may not sell or modify the content of this Website or reproduce, display, publicly perform, distribute,\n" +
                    "or otherwise use the materials in any way for any public or commercial purpose without the respective\n" +
                    "organisation’s or entity’s written permission.<br><br>\n" +
                    "<b>ACCEPTABLE WEBSITE USE</b><br><br>\n" +
                    "<b>(A) Security Rules</b><br><br>\n" +
                    "Visitors are prohibited from violating or attempting to violate the security of the Web site, including,\n" +
                    "without limitation, (1) accessing data not intended for such user or logging into a server or account which\n" +
                    "the user is not authorised to access, (2) attempting to probe, scan or test the vulnerability of a system or\n" +
                    "network or to breach security or authentication measures without proper authorisation, (3) attempting to\n" +
                    "interfere with service to any user, host or network, including, without limitation, via means of submitting a\n" +
                    "virus or &quot;Trojan horse&quot; to the Website, overloading, &quot;flooding&quot;, &quot;mail bombing&quot; or &quot;crashing&quot;, or (4)\n" +
                    "sending unsolicited electronic mail, including promotions and/or advertising of products or services.\n" +
                    "Violations of system or network security may result in civil or criminal liability. The business and / or its\n" +
                    "associate entities will have the right to investigate occurrences that they suspect as involving such\n" +
                    "violations and will have the right to involve, and cooperate with, law enforcement authorities in\n" +
                    "prosecuting users who are involved in such violations.<br><br>\n" +
                    "<b>(B) General Rules</b><br><br>\n" +
                    "Visitors may not use the Web Site in order to transmit, distribute, store or destroy material (a) that could\n" +
                    "constitute or encourage conduct that would be considered a criminal offence or violate any applicable law\n" +
                    "or regulation, (b) in a manner that will infringe the copyright, trademark, trade secret or other intellectual\n" +
                    "property rights of others or violate the privacy or publicity of other personal rights of others, or (c) that is\n" +
                    "libellous, defamatory, pornographic, profane, obscene, threatening, abusive or hateful.<br><br>\n" +
                    "<b>INDEMNITY</b><br><br>\n" +
                    "The User unilaterally agree to indemnify and hold harmless, without objection, the Company, its officers,\n" +
                    "directors, employees and agents from and against any claims, actions and/or demands and/or liabilities\n" +
                    "\n" +
                    "and/or losses and/or damages whatsoever arising from or resulting from their use of bazinga\n" +
                    "application or their breach of the terms .<br><br>\n" +
                    "<b>LIABILITY</b><br><br>\n" +
                    "User agrees that neither Company nor its group companies, directors, officers or employee shall be liable\n" +
                    "for any direct or/and indirect or/and incidental or/and special or/and consequential or/and exemplary\n" +
                    "damages, resulting from the use or/and the inability to use the service or/and for cost of procurement of\n" +
                    "substitute goods or/and services or resulting from any goods or/and data or/and information or/and\n" +
                    "services purchased or/and obtained or/and messages received or/and transactions entered into through\n" +
                    "or/and from the service or/and resulting from unauthorized access to or/and alteration of user&#39;s\n" +
                    "transmissions or/and data or/and arising from any other matter relating to the service, including but not\n" +
                    "limited to, damages for loss of profits or/and use or/and data or other intangible, even if Company has\n" +
                    "been advised of the possibility of such damages.<br><br>\n" +
                    "User further agrees that Company shall not be liable for any damages arising from interruption,\n" +
                    "suspension or termination of service, including but not limited to direct or/and indirect or/and incidental\n" +
                    "or/and special consequential or/and exemplary damages, whether such interruption or/and suspension\n" +
                    "or/and termination was justified or not, negligent or intentional, inadvertent or advertent.<br><br>\n" +
                    "User agrees that Company shall not be responsible or liable to user, or anyone, for the statements or\n" +
                    "conduct of any third party of the service. In sum, in no event shall Company&#39;s total liability to the User for\n" +
                    "all damages or/and losses or/and causes of action exceed the amount paid by the User to Company, if\n" +
                    "any, that is related to the cause of action.<br><br>\n" +
                    "<b>DISCLAIMER OF CONSEQUENTIAL DAMAGES</b><br><br>\n" +
                    "In no event shall Company or any parties, organizations or entities associated with the corporate brand\n" +
                    "name us or otherwise, mentioned at this Website be liable for any damages whatsoever (including,\n" +
                    "without limitations, incidental and consequential damages, lost profits, or damage to computer hardware\n" +
                    "or loss of data information or business interruption) resulting from the use or inability to use the Website\n" +
                    "and the Website material, whether based on warranty, contract, tort, or any other legal theory, and\n" +
                    "whether or not, such organization or entities were advised of the possibility of such damages.<br><br></p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml("<h2><b>TERMS AND CONDITIONS</h2></b><br><br><p>The terms &quot;We&quot; / &quot;Us&quot; / &quot;Our&quot;/”Company” individually and collectively refer to KRV Hospitalities and the\n" +
                    "terms &quot;Visitor” ”User” refer to the users.<br><br>\n" +
                    "This page states the Terms and Conditions under which you (Visitor) may visit this Application\n" +
                    "(“Bazinga”). Please read this page carefully. If you do not accept the Terms and Conditions stated here,\n" +
                    "we would request you to exit this site. The business, any of its business divisions and / or its subsidiaries,\n" +
                    "associate companies or subsidiaries to subsidiaries or such other investment companies (in India or\n" +
                    "abroad) reserve their respective rights to revise these Terms and Conditions at any time by updating this\n" +
                    "posting. You should visit this page periodically to re-appraise yourself of the Terms and Conditions,\n" +
                    "because they are binding on all users of this Website.<br><br>\n" +
                    "<b>USE OF CONTENT</b><br><br>\n" +
                    "All logos, brands, marks headings, labels, names, signatures, numerals, shapes or any combinations\n" +
                    "thereof, appearing in this site, except as otherwise noted, are properties either owned, or used under\n" +
                    "licence, by the business and / or its associate entities who feature on this Website. The use of these\n" +
                    "properties or any other content on this site, except as provided in these terms and conditions or in the site\n" +
                    "content, is strictly prohibited.<br><br>\n" +
                    "You may not sell or modify the content of this Website or reproduce, display, publicly perform, distribute,\n" +
                    "or otherwise use the materials in any way for any public or commercial purpose without the respective\n" +
                    "organisation’s or entity’s written permission.<br><br>\n" +
                    "<b>ACCEPTABLE WEBSITE USE</b><br><br>\n" +
                    "<b>(A) Security Rules</b><br><br>\n" +
                    "Visitors are prohibited from violating or attempting to violate the security of the Web site, including,\n" +
                    "without limitation, (1) accessing data not intended for such user or logging into a server or account which\n" +
                    "the user is not authorised to access, (2) attempting to probe, scan or test the vulnerability of a system or\n" +
                    "network or to breach security or authentication measures without proper authorisation, (3) attempting to\n" +
                    "interfere with service to any user, host or network, including, without limitation, via means of submitting a\n" +
                    "virus or &quot;Trojan horse&quot; to the Website, overloading, &quot;flooding&quot;, &quot;mail bombing&quot; or &quot;crashing&quot;, or (4)\n" +
                    "sending unsolicited electronic mail, including promotions and/or advertising of products or services.\n" +
                    "Violations of system or network security may result in civil or criminal liability. The business and / or its\n" +
                    "associate entities will have the right to investigate occurrences that they suspect as involving such\n" +
                    "violations and will have the right to involve, and cooperate with, law enforcement authorities in\n" +
                    "prosecuting users who are involved in such violations.<br><br>\n" +
                    "<b>(B) General Rules</b><br><br>\n" +
                    "Visitors may not use the Web Site in order to transmit, distribute, store or destroy material (a) that could\n" +
                    "constitute or encourage conduct that would be considered a criminal offence or violate any applicable law\n" +
                    "or regulation, (b) in a manner that will infringe the copyright, trademark, trade secret or other intellectual\n" +
                    "property rights of others or violate the privacy or publicity of other personal rights of others, or (c) that is\n" +
                    "libellous, defamatory, pornographic, profane, obscene, threatening, abusive or hateful.<br><br>\n" +
                    "<b>INDEMNITY</b><br><br>\n" +
                    "The User unilaterally agree to indemnify and hold harmless, without objection, the Company, its officers,\n" +
                    "directors, employees and agents from and against any claims, actions and/or demands and/or liabilities\n" +
                    "\n" +
                    "and/or losses and/or damages whatsoever arising from or resulting from their use of bazinga\n" +
                    "application or their breach of the terms .<br><br>\n" +
                    "<b>LIABILITY</b><br><br>\n" +
                    "User agrees that neither Company nor its group companies, directors, officers or employee shall be liable\n" +
                    "for any direct or/and indirect or/and incidental or/and special or/and consequential or/and exemplary\n" +
                    "damages, resulting from the use or/and the inability to use the service or/and for cost of procurement of\n" +
                    "substitute goods or/and services or resulting from any goods or/and data or/and information or/and\n" +
                    "services purchased or/and obtained or/and messages received or/and transactions entered into through\n" +
                    "or/and from the service or/and resulting from unauthorized access to or/and alteration of user&#39;s\n" +
                    "transmissions or/and data or/and arising from any other matter relating to the service, including but not\n" +
                    "limited to, damages for loss of profits or/and use or/and data or other intangible, even if Company has\n" +
                    "been advised of the possibility of such damages.<br><br>\n" +
                    "User further agrees that Company shall not be liable for any damages arising from interruption,\n" +
                    "suspension or termination of service, including but not limited to direct or/and indirect or/and incidental\n" +
                    "or/and special consequential or/and exemplary damages, whether such interruption or/and suspension\n" +
                    "or/and termination was justified or not, negligent or intentional, inadvertent or advertent.<br><br>\n" +
                    "User agrees that Company shall not be responsible or liable to user, or anyone, for the statements or\n" +
                    "conduct of any third party of the service. In sum, in no event shall Company&#39;s total liability to the User for\n" +
                    "all damages or/and losses or/and causes of action exceed the amount paid by the User to Company, if\n" +
                    "any, that is related to the cause of action.<br><br>\n" +
                    "<b>DISCLAIMER OF CONSEQUENTIAL DAMAGES</b><br><br>\n" +
                    "In no event shall Company or any parties, organizations or entities associated with the corporate brand\n" +
                    "name us or otherwise, mentioned at this Website be liable for any damages whatsoever (including,\n" +
                    "without limitations, incidental and consequential damages, lost profits, or damage to computer hardware\n" +
                    "or loss of data information or business interruption) resulting from the use or inability to use the Website\n" +
                    "and the Website material, whether based on warranty, contract, tort, or any other legal theory, and\n" +
                    "whether or not, such organization or entities were advised of the possibility of such damages.<br><br></p>"));
        }




    }

    @Override
    public void onBackPressed() {
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(this, contact_us.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(TandC.this, contact_us.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
