package app.fpp.model.am.client;

import app.fpp.model.am.common.UserAccessAM;

import oracle.jbo.client.remote.ApplicationModuleImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Jul 22 08:13:46 ICT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class UserAccessAMClient extends ApplicationModuleImpl implements UserAccessAM {
    /**
     * This is the default constructor (do not remove).
     */
    public UserAccessAMClient() {
    }

    public void changePassword(String userId, String newPassword) {
        Object _ret =
            this.riInvokeExportedMethod(this,"changePassword",new String [] {"java.lang.String","java.lang.String"},new Object[] {userId, newPassword});
        return;
    }
}
