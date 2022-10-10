import React from "react";
import styles from './Reg.module.css'
import eye from './眼睛_隐藏_o.svg'
class Register extends React.Component {
    state = {
        enableSendCode: true,
        time: 60
    }

    CountTime = () => {
        this.setState({
            enableSendCode: false
        })
        var timeClock;
        var t = 60
        timeClock = setInterval(() => {
            t--;

            this.setState({
                time: t
            })
            if (t === 0) {
                this.setState({
                    enableSendCode: true,
                    time: 60
                })
                clearInterval(timeClock)
            }
        }, 1000)
    }

    render() {
        let sendBtn
        if (this.state.enableSendCode) {
            sendBtn = <button className={styles.sendBtn1} onClick={this.CountTime}>send an identifying code</button>
        }
        else {
            sendBtn = <button className={styles.sendBtn2} disabled>remain {this.state.time} seconds</button>
        }
        return (
            <div className={styles.regBox}>
                <div className={styles.sendBtnBox}>{sendBtn}</div>

                <div>
                    <form className={styles.RegTab} name="RegTab" action="#">
                        <table>
                            <tbody>
                                <tr className={styles.tableRow1}>
                                    <td>
                                        <div>
                                            <div className={styles.noticeInfo}>FirstName</div>
                                            <input className={styles.inputText1} type='text'></input>
                                        </div>
                                    </td>
                                    <td>
                                        <div>
                                            <div className={styles.noticeInfo}>LastName</div>
                                            <input className={styles.inputText1} type='text'></input>
                                        </div>
                                    </td>
                                </tr>

                                <tr className={styles.tableRow2}>
                                    <td>
                                        <div className={styles.noticeInfo}>District</div>
                                        <select className={styles.selectDist}>
                                            <option>China</option>
                                            <option>Japan</option>
                                            <option>England</option>
                                            <option>America</option>
                                            <option>France</option>
                                        </select>
                                    </td>

                                </tr>
                                <tr className={styles.tableRow3}>
                                    <td>
                                        <div className={styles.noticeInfo}>PhoneNumber</div>
                                        <select className={styles.selectNum}>
                                            <option>+86</option>
                                            <option>+123</option>
                                            <option>+124</option>
                                            <option>+125</option>
                                            <option>+126</option>
                                        </select>
                                        <input className={styles.inputTel} type='text' maxLength={11}></input>
                                        <span className={styles.noticeInfo1}>We will send an identifying code to this phone.</span>
                                    </td>
                                </tr>
                                <tr className={styles.tableRow1}>
                                    <td>
                                        <div className={styles.noticeInfo}>Password</div>
                                        <input className={styles.inputText1} type='password'></input>
                                        <span className={styles.seePwd}><img src={eye}></img></span>
                                    </td>
                                    <td>
                                        <div className={styles.noticeInfo}>Identify password</div>
                                        <input className={styles.inputText1} type='password'></input>
                                        <span className={styles.seePwd}><img src={eye}></img></span>
                                    </td>
                                </tr>

                            </tbody>
                        </table>






                    </form>
                    <div className={styles.btnsBox}>
                        <button className={styles.resetBtn}>reset</button>
                    </div>
                    <div className={styles.btnsBox}>
                        <button className={styles.joinBtn}>join</button>
                    </div>
                    <div className={styles.btnsBox}>
                        <button className={styles.cancelBtn}>cancel</button>
                    </div>
                </div>

            </div>
        )
    }
}

export default Register