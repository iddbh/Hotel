import React from "react";
import styles from './Login.module.css'
class Login extends React.Component {

    render() {

        return (
            <div>


                <div className={styles.Loginbox}>
                    <div className={styles.loginTitle}><h1 >Log in</h1></div>

                    <div className={styles.loginForm}>
                        <form className={styles.loginTab} name="LoginTab" action="#" >
                            <table>
                                <tbody>
                                    <tr>
                                        <div className={styles.formRow1}>
                                            <span className={styles.noticeInfo1}>name:</span>
                                            <input className={styles.inputText1} type='text' placeholder="phone number or email"></input>
                                        </div>
                                    </tr>
                                    <tr>
                                        <div className={styles.formRow1}>
                                            <span className={styles.noticeInfo2}>password:</span>
                                            <input className={styles.inputText2} type='password'  placeholder="password"></input>
                                        </div>
                                    </tr>
                                </tbody>
                            </table>
                            <div className={styles.btnBox}>
                                <input type='submit' value='submit' className={styles.btn1}></input>
                            <input type='reset' value='reset' className={styles.btn1}></input>
                            </div>
                            
                        </form>

                    </div>
                    <a href="#" className={styles.forgetNotice}>forget your password?</a>


                    
                </div>

                <div className={styles.Regbox}>
                    <div className={styles.slogan}>
                        <ul>
                            <span className={styles.sloganLi}>
                                -enjoy free Wi-Fi!
                            </span>
                            <span className={styles.sloganLi}>
                                -more present!
                            </span><br />
                            <span className={styles.sloganLi}>
                                -Irregular discount!
                            </span>
                            <span className={styles.sloganLi}>
                                -for more benefits, join now!
                            </span>
                        </ul>
                    </div>
                    <a className={styles.head1} href="#">Join us!</a>
                </div>

            </div>
        )
    }


}

export default Login