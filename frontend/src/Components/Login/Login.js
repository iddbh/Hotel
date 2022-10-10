import React from "react";
import styles from './Login.module.css'
class Login extends React.Component {

    render() {

        return (
            <>
                <h1>Welcome!</h1>
                <div>
                    <div className={styles.Loginbox}>
                        <h1>Log in</h1>
                        <form className={styles.LoginTab} name="LoginTab" action="#" >
                            <table>
                                <tbody>
                                    <tr>
                                        <td className={styles.subtitle}>UserName:</td>
                                        <td><input type='text' placeholder="your phone number or mail address"></input></td>
                                    </tr>
                                    <tr>
                                        <td className={styles.subtitle}>Password:</td>
                                        <td><input type='password' placeholder="password" ></input></td>
                                    </tr>
                                </tbody>
                            </table>
                            <input type='submit' value='submit' className={styles.subBtn}></input>
                            <input type='reset' value='reset'></input>
                        </form>
                        <a href="#">forget your password?</a>
                    </div>
                    <div className={styles.Regbox}>
                        <h1>Join us!</h1>
                        <div className="pic"></div>
                        <ul>
                            <li>
                                enjoy free Wi-Fi!
                            </li>
                            <li>
                                more present!
                            </li>
                            <li>
                                Irregular discount!
                            </li>
                        </ul>
                        <a href="#">Register</a>
                    </div>
                </div>
            </>
        )
    }


}

export default Login