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

public class Privacy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.abouttoolbar);
        toolbar.setTitle("Privacy Policy");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView=(TextView)findViewById(R.id.contacttext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<h2><b>Privacy Policy</h2></b><br><br><p>Your website may use the Privacy Policy given below:<br>\n" +
                    "The terms &quot;We&quot; / &quot;Us&quot; / &quot;Our&quot;/”Company” individually and collectively refer to KRV Hospitalities and the\n" +
                    "terms &quot;You&quot; /&quot;Your&quot; / &quot;Yourself&quot; refer to the users.<br><br>\n" +
                    "This Privacy Policy is an electronic record in the form of an electronic contract formed under the information\n" +
                    "Technology Act, 2000 and the rules made thereunder and the amended provisions pertaining to electronic\n" +
                    "documents / records in various statutes as amended by the information Technology Act, 2000. This Privacy\n" +
                    "Policy does not require any physical, electronic or digital signature.<br><br>\n" +
                    "This Privacy Policy is a legally binding document between you and KRV Hospitalities (both terms defined\n" +
                    "below). The terms of this Privacy Policy will be effective upon your acceptance of the same (directly or\n" +
                    "indirectly in electronic form, by clicking on the I accept tab or by use of the Application or by other means)\n" +
                    "and will govern the relationship between you and KRV Hospitalities for your use of the Application\n" +
                    "“Bazinga” (defined below).<br><br>\n" +
                    "This document is published and shall be construed in accordance with the provisions of the Information\n" +
                    "Technology (reasonable security practices and procedures and sensitive personal data of information) rules,\n" +
                    "2011 under Information Technology Act, 2000; that require publishing of the Privacy Policy for collection,\n" +
                    "use, storage and transfer of sensitive personal data or information.<br><br>\n" +
                    "Please read this Privacy Policy carefully by using the Application, you indicate that you understand, agree\n" +
                    "and consent to this Privacy Policy. If you do not agree with the terms of this Privacy Policy, please do not\n" +
                    "use this Application.<br><br>\n" +
                    "By providing us your Information or by making use of the facilities provided by the Application, You hereby\n" +
                    "consent to the collection, storage, processing and transfer of any or all of Your Personal Information and\n" +
                    "Non-Personal Information by us as specified under this Privacy Policy. You further agree that such\n" +
                    "collection, use, storage and transfer of Your Information shall not cause any loss or wrongful gain to you or\n" +
                    "any other person.<br><br>\n" +
                    "<b>USER INFORMATION </b><br><br>\n" +
                    "To avail certain services on our Websites, users are required to provide certain information for the registration\n" +
                    "process namely: - a) your name, b) email address, c) sex, d) age, e) PIN code, f) credit card or debit card\n" +
                    "details g) medical records and history h) sexual orientation, i) biometric information, j) password etc., and / or\n" +
                    "your occupation, interests, and the like. The Information as supplied by the users enables us to improve our\n" +
                    "sites and provide you the most user-friendly experience.<br><br>\n" +
                    "All required information is service dependent and we may use the above said user information to, maintain,\n" +
                    "protect, and improve its services (including advertising services) and for developing new services<br>\n" +
                    "Such information will not be considered as sensitive if it is freely available and accessible in the public domain\n" +
                    "or is furnished under the Right to Information Act, 2005 or any other law for the time being in force.<br>\n" +
                    "COOKIES<br><br>\n" +
                    "To improve the responsiveness of the sites for our users, we may use &quot;cookies&quot;, or similar electronic tools to\n" +
                    "collect information to assign each visitor a unique, random number as a User Identification (User ID) to\n" +
                    "understand the user&#39;s individual interests using the Identified Computer. Unless you voluntarily identify\n" +
                    "yourself (through registration, for example), we will have no way of knowing who you are, even if we assign a\n" +
                    "cookie to your computer. The only personal information a cookie can contain is information you supply (an\n" +
                    "example of this is when you ask for our Personalised Horoscope). A cookie cannot read data off your hard\n" +
                    "drive. Our advertisers may also assign their own cookies to your browser (if you click on their ads), a process\n" +
                    "that we do not control.<br><br> \n" +
                    "\n" +
                    "Our web servers automatically collect limited information about your computer&#39;s connection to the Internet,\n" +
                    "including your IP address, when you visit our site. (Your IP address is a number that lets computers attached\n" +
                    "to the Internet know where to send you data -- such as the web pages you view.) Your IP address does not\n" +
                    "identify you personally. We use this information to deliver our web pages to you upon request, to tailor our site\n" +
                    "to the interests of our users, to measure traffic within our site and let advertisers know the geographic\n" +
                    "locations from where our visitors come.<br><br> \n" +
                    "<b>LINKS TO THE OTHER SITES</b><br><br>\n" +
                    "Our policy discloses the privacy practices for our own web site only. Our site provides links to other websites\n" +
                    "also that are beyond our control. We shall in no way be responsible in way for your use of such sites.<br><br>\n" +
                    "<b>INFORMATION SHARING</b>\n<br><br>" +
                    "We shares the sensitive personal information to any third party without obtaining the prior consent of the\n" +
                    "user in the following limited circumstances:<br><br>\n" +
                    "(a) When it is requested or required by law or by any court or governmental agency or authority to disclose,\n" +
                    "for the purpose of verification of identity, or for the prevention, detection, investigation including cyber\n" +
                    "incidents, or for prosecution and punishment of offences. These disclosures are made in good faith and\n" +
                    "belief that such disclosure is reasonably necessary for enforcing these Terms; for complying with the\n" +
                    "applicable laws and regulations.<br><br> \n" +
                    "(b) We proposes to share such information within its group companies and officers and employees of such\n" +
                    "group companies for the purpose of processing personal information on its behalf. We also ensure that these\n" +
                    "recipients of such information agree to process such information based on our instructions and in compliance\n" +
                    "with this Privacy Policy and any other appropriate confidentiality and security measures.<br><br>\n" +
                    "<b>INFORMATION SECURITY</b><br><br>\n" +
                    "We take appropriate security measures to protect against unauthorized access to or unauthorized alteration,\n" +
                    "disclosure or destruction of data. These include internal reviews of our data collection, storage and\n" +
                    "processing practices and security measures, including appropriate encryption and physical security\n" +
                    "measures to guard against unauthorized access to systems where we store personal data.<br><br>\n" +
                    "All information gathered on our Website is securely stored within our controlled database. The database is\n" +
                    "stored on servers secured behind a firewall; access to the servers is password-protected and is strictly\n" +
                    "limited. However, as effective as our security measures are, no security system is impenetrable. We cannot\n" +
                    "guarantee the security of our database, nor can we guarantee that information you supply will not be\n" +
                    "intercepted while being transmitted to us over the Internet. And, of course, any information you include in a\n" +
                    "posting to the discussion areas is available to anyone with Internet access.<br><br> \n" +
                    "However the internet is an ever evolving medium. We may change our Privacy Policy from time to time to\n" +
                    "incorporate necessary future changes. Of course, our use of any information we gather will always be\n" +
                    "consistent with the policy under which the information was collected, regardless of what the new policy may\n" +
                    "be. <br><br>\n" +
                    "<b>Grievance Redressal</b><br><br>\n" +
                    "Redressal Mechanism: Any complaints, abuse or concerns with regards to content and or comment or\n" +
                    "breach of these terms shall be immediately informed to the designated Grievance Officer as mentioned\n" +
                    "below via in writing or through email signed with the electronic signature to Shalabh Gupta (&quot;Grievance\n" +
                    "Officer&quot;).<br><br> \n" +
                    "<b>Mr. Shalabh Gupta </b>(Grievance Officer)<br>\n" +
                    "https://play.google.com/store/apps/details?id=com.manish.bazingalnmiit&amp;hl=en_IN\n<br>" +
                    "Company Name &amp; Address : <b>KRV Hospitalities</b> &amp;<b> Bazinga , LNMIIT ,Jaipur, Rajasthan 302031</b><br>\n" +
                    "Email: shalabh1221@gmail.com<br>\n" +
                    "Ph: <b>8077543588</b><br></p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml("<h2><b>Privacy Policy</h2></b><br><br><p>Your website may use the Privacy Policy given below:<br>\n" +
                    "The terms &quot;We&quot; / &quot;Us&quot; / &quot;Our&quot;/”Company” individually and collectively refer to KRV Hospitalities and the\n" +
                    "terms &quot;You&quot; /&quot;Your&quot; / &quot;Yourself&quot; refer to the users.<br><br>\n" +
                    "This Privacy Policy is an electronic record in the form of an electronic contract formed under the information\n" +
                    "Technology Act, 2000 and the rules made thereunder and the amended provisions pertaining to electronic\n" +
                    "documents / records in various statutes as amended by the information Technology Act, 2000. This Privacy\n" +
                    "Policy does not require any physical, electronic or digital signature.<br><br>\n" +
                    "This Privacy Policy is a legally binding document between you and KRV Hospitalities (both terms defined\n" +
                    "below). The terms of this Privacy Policy will be effective upon your acceptance of the same (directly or\n" +
                    "indirectly in electronic form, by clicking on the I accept tab or by use of the Application or by other means)\n" +
                    "and will govern the relationship between you and KRV Hospitalities for your use of the Application\n" +
                    "“Bazinga” (defined below).<br><br>\n" +
                    "This document is published and shall be construed in accordance with the provisions of the Information\n" +
                    "Technology (reasonable security practices and procedures and sensitive personal data of information) rules,\n" +
                    "2011 under Information Technology Act, 2000; that require publishing of the Privacy Policy for collection,\n" +
                    "use, storage and transfer of sensitive personal data or information.<br><br>\n" +
                    "Please read this Privacy Policy carefully by using the Application, you indicate that you understand, agree\n" +
                    "and consent to this Privacy Policy. If you do not agree with the terms of this Privacy Policy, please do not\n" +
                    "use this Application.<br><br>\n" +
                    "By providing us your Information or by making use of the facilities provided by the Application, You hereby\n" +
                    "consent to the collection, storage, processing and transfer of any or all of Your Personal Information and\n" +
                    "Non-Personal Information by us as specified under this Privacy Policy. You further agree that such\n" +
                    "collection, use, storage and transfer of Your Information shall not cause any loss or wrongful gain to you or\n" +
                    "any other person.<br><br>\n" +
                    "<b>USER INFORMATION </b><br><br>\n" +
                    "To avail certain services on our Websites, users are required to provide certain information for the registration\n" +
                    "process namely: - a) your name, b) email address, c) sex, d) age, e) PIN code, f) credit card or debit card\n" +
                    "details g) medical records and history h) sexual orientation, i) biometric information, j) password etc., and / or\n" +
                    "your occupation, interests, and the like. The Information as supplied by the users enables us to improve our\n" +
                    "sites and provide you the most user-friendly experience.<br><br>\n" +
                    "All required information is service dependent and we may use the above said user information to, maintain,\n" +
                    "protect, and improve its services (including advertising services) and for developing new services<br>\n" +
                    "Such information will not be considered as sensitive if it is freely available and accessible in the public domain\n" +
                    "or is furnished under the Right to Information Act, 2005 or any other law for the time being in force.<br>\n" +
                    "COOKIES<br><br>\n" +
                    "To improve the responsiveness of the sites for our users, we may use &quot;cookies&quot;, or similar electronic tools to\n" +
                    "collect information to assign each visitor a unique, random number as a User Identification (User ID) to\n" +
                    "understand the user&#39;s individual interests using the Identified Computer. Unless you voluntarily identify\n" +
                    "yourself (through registration, for example), we will have no way of knowing who you are, even if we assign a\n" +
                    "cookie to your computer. The only personal information a cookie can contain is information you supply (an\n" +
                    "example of this is when you ask for our Personalised Horoscope). A cookie cannot read data off your hard\n" +
                    "drive. Our advertisers may also assign their own cookies to your browser (if you click on their ads), a process\n" +
                    "that we do not control.<br><br> \n" +
                    "\n" +
                    "Our web servers automatically collect limited information about your computer&#39;s connection to the Internet,\n" +
                    "including your IP address, when you visit our site. (Your IP address is a number that lets computers attached\n" +
                    "to the Internet know where to send you data -- such as the web pages you view.) Your IP address does not\n" +
                    "identify you personally. We use this information to deliver our web pages to you upon request, to tailor our site\n" +
                    "to the interests of our users, to measure traffic within our site and let advertisers know the geographic\n" +
                    "locations from where our visitors come.<br><br> \n" +
                    "<b>LINKS TO THE OTHER SITES</b><br><br>\n" +
                    "Our policy discloses the privacy practices for our own web site only. Our site provides links to other websites\n" +
                    "also that are beyond our control. We shall in no way be responsible in way for your use of such sites.<br><br>\n" +
                    "<b>INFORMATION SHARING</b>\n<br><br>" +
                    "We shares the sensitive personal information to any third party without obtaining the prior consent of the\n" +
                    "user in the following limited circumstances:<br><br>\n" +
                    "(a) When it is requested or required by law or by any court or governmental agency or authority to disclose,\n" +
                    "for the purpose of verification of identity, or for the prevention, detection, investigation including cyber\n" +
                    "incidents, or for prosecution and punishment of offences. These disclosures are made in good faith and\n" +
                    "belief that such disclosure is reasonably necessary for enforcing these Terms; for complying with the\n" +
                    "applicable laws and regulations.<br><br> \n" +
                    "(b) We proposes to share such information within its group companies and officers and employees of such\n" +
                    "group companies for the purpose of processing personal information on its behalf. We also ensure that these\n" +
                    "recipients of such information agree to process such information based on our instructions and in compliance\n" +
                    "with this Privacy Policy and any other appropriate confidentiality and security measures.<br><br>\n" +
                    "<b>INFORMATION SECURITY</b><br><br>\n" +
                    "We take appropriate security measures to protect against unauthorized access to or unauthorized alteration,\n" +
                    "disclosure or destruction of data. These include internal reviews of our data collection, storage and\n" +
                    "processing practices and security measures, including appropriate encryption and physical security\n" +
                    "measures to guard against unauthorized access to systems where we store personal data.<br><br>\n" +
                    "All information gathered on our Website is securely stored within our controlled database. The database is\n" +
                    "stored on servers secured behind a firewall; access to the servers is password-protected and is strictly\n" +
                    "limited. However, as effective as our security measures are, no security system is impenetrable. We cannot\n" +
                    "guarantee the security of our database, nor can we guarantee that information you supply will not be\n" +
                    "intercepted while being transmitted to us over the Internet. And, of course, any information you include in a\n" +
                    "posting to the discussion areas is available to anyone with Internet access.<br><br> \n" +
                    "However the internet is an ever evolving medium. We may change our Privacy Policy from time to time to\n" +
                    "incorporate necessary future changes. Of course, our use of any information we gather will always be\n" +
                    "consistent with the policy under which the information was collected, regardless of what the new policy may\n" +
                    "be. <br><br>\n" +
                    "<b>Grievance Redressal</b><br><br>\n" +
                    "Redressal Mechanism: Any complaints, abuse or concerns with regards to content and or comment or\n" +
                    "breach of these terms shall be immediately informed to the designated Grievance Officer as mentioned\n" +
                    "below via in writing or through email signed with the electronic signature to Shalabh Gupta (&quot;Grievance\n" +
                    "Officer&quot;).<br><br> \n" +
                    "<b>Mr. Shalabh Gupta </b>(Grievance Officer)<br>\n" +
                    "https://play.google.com/store/apps/details?id=com.manish.bazingalnmiit&amp;hl=en_IN\n<br>" +
                    "Company Name &amp; Address : <b>KRV Hospitalities</b> &amp;<b> Bazinga , LNMIIT ,Jaipur, Rajasthan 302031</b><br>\n" +
                    "Email: shalabh1221@gmail.com<br>\n" +
                    "Ph: <b>8077543588</b><br></p>"));
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
            Intent intent = new Intent(Privacy.this, contact_us.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
