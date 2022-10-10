import { useState } from 'react';
import styles from './LocationBar.module.css';
import closeBtn from './关闭.svg'


// 搜索定位栏
const LocationBar = () => {

    // 设置状态完成编辑栏的弹出和隐藏
    const [editDetails, setEditDetails] = useState(false);

    //设置函数
    const editDetailsHandler = () => {
        setEditDetails(prevState => !prevState);
    }

    
    const [inputDest, setInputDest] = useState('中国,深圳');
    const [inputDate1, setInputDate1] = useState('');
    const [inputDate2, setInputDate2] = useState('');
    const [inputRoom, setInputRoom] = useState('大床房');
    const [inputGuestNum, setInputGuestNum] = useState(2);





    // 监听编辑栏中目的地变化
    const destChangeHandler = (event) => {
        setInputDest(event.target.value);
        // inputDest = event.target.value;

    }
    // 监听编辑栏中入住日期变化
    const dateChangeHandler1 = (event) => {
        setInputDate1(event.target.value);

    }
    // 监听编辑栏中退房日期变化
    const dateChangeHandler2 = (event) => {
        setInputDate2(event.target.value);

    }// 监听编辑栏中客房变化
    const roomChangeHandler = (event) => {
        setInputRoom(event.target.value);

    }
    // 监听编辑栏中人数变化
    const guestNumChangeHandler = (event) => {
        setInputGuestNum(event.target.value);

    }

    


    const LocationInfo = () => {
        return <div className={styles.locationInfo}>
            {/* 目的地信息 */}
            <div className={styles.destInfo}>
                <div className={styles.destInfo1}>目的地</div>
                <div className={styles.destInfo2}>{inputDest}</div>
            </div>
            {/* 住宿信息 */}
            <div className={styles.lodgingInfo}>
                <div className={styles.lodgingInfo1}>住宿日期</div>
                <div className={styles.lodgingInfo2}>{inputDate1} 到 {inputDate2}</div>
            </div>
            {/* 客房信息 */}
            <div className={styles.roomInfo}>
                <div className={styles.roomInfo1}>客房及宾客</div>
                <div className={styles.roomInfo2}>{inputRoom} / {inputGuestNum}</div>

            </div>
            {/* 编辑按钮 */}
            <button className={styles.editInfoBtn} onClick={editDetailsHandler}>编辑</button>
        </div>
    }

    const EditInfo = () => {
        return <div className={styles.editInfo}>
            {/* 在最上方放一个盒子，放关闭按钮 */}
            <div className={styles.closeArea}>
                <button className={styles.closeBtn} onClick={editDetailsHandler}>
                    <img src={closeBtn}></img>
                </button>
            </div>
            <div className={styles.formBlock}>
                <div className={styles.editBlock}>
                    <div className={styles.editNotice} htmlFor='addr'>目的地</div>
                    <select value={inputDest} onChange={destChangeHandler} className={styles.addrText} id ='addr'>
                        <option >中国，深圳</option>
                        <option>美国, 纽约</option>
                        <option>日本, 东京</option>
                        <option>荷兰, 阿姆斯特丹</option>
                    </select>
                    
                </div>
                <div className={styles.editBlock}>
                    <div className={styles.editNotice} htmlFor='date'>住宿日期</div>
                    <input value={inputDate1} onChange={dateChangeHandler1} className={styles.dateText} id='date' type='date'></input>
                    <span> 到 </span>
                    <input value={inputDate2} onChange={dateChangeHandler2} className={styles.dateText} id='date' type='date'></input>

                </div>
                <div className={styles.editBlock}>
                    <div className={styles.editNotice} htmlFor='room'>客房及宾客</div>
                    <select value={inputRoom} onChange={roomChangeHandler} className={styles.roomText} id='room'>
                        <option>大床房</option>
                        <option>双人房</option>
                        <option>标准套房</option>
                        <option>总统套房</option>

                    </select>
                    {/* <input value={inputRoom} onChange={roomChangeHandler} className={styles.roomText} id='room' type='text' placeholder='请选择客房'></input> */}
                    <input value={inputGuestNum} onChange={guestNumChangeHandler} className={styles.roomText} id='room' type='number' placeholder='请输入人数'></input>
                </div>
            </div>

            <div className={styles.searchArea}>
                {/* <button className={styles.sureBtn}>确定</button> */}
                <button className={styles.searchBtn}>查找酒店</button>
            </div>


        </div>

    }

    const MapInfo = () => {
        return <div className={styles.mapInfo}>
        </div>
    }




    // 定位栏整体
    return <div className={styles.locationBar}>
        {/* 详细内容，只有在未点击时显示 */}
        {!editDetails && <LocationInfo />}
        {/* 表单，在点击编辑按钮后才会出现，用于编辑客户信息 */}
        {editDetails && <EditInfo />}
        {/* 地图栏 */}
        <MapInfo />



    </div>

}


export default LocationBar;