import styles from './Hotel.module.css'

const Hotel = () => {
    //酒店整体
    return <div className={styles.hotelBlocks}>
        {/* 酒店信息 */}
        <div className={styles.hotelInfo}>
            {/* 图片 */}
            <div className={styles.hotelImg}>
                <a href='#'>
                    <img src='./logo192.png'/>
                </a>
            </div>
            {/* 酒店详细信息 */}
            <div className={styles.hotelDetail}>
                {/* 酒店名称 */}
                <div className={styles.hotelName}>
                    <a href='#'>
                        {/* 酒店中文名称 */}
                        <div className={styles.hotelNameCN}>塘朗大酒店专家公寓</div>
                        {/* 酒店英文名称 */}
                        <div className={styles.hotelNameEN}>TangLang Hotel faculty only</div>
                    </a>
                                       
                </div>
                {/* 地址信息 */}
                <div className={styles.hotelAddr}>
                    深圳市南山区桃园街道1088号
                </div>
                {/* 文字简介 */}
                <div className={styles.hotelIntro}>
                欢迎莅临下榻安吉 JW 万豪酒店，在中国竹乡畅享惬意休闲体验，
                在高尔夫俱乐部尽情挥杆试技。
                </div>
                


            </div>
            {/* 酒店logo */}
            <div className={styles.hotelLogo}>
            <img src='./logo192.png'/>
            </div>


        </div>
        {/* 酒店预订 */}
        <div className={styles.hotelBooking}>
            <button className={styles.hotelBookingBtn}>立即预订</button>
        </div>
        
    </div>

}

export default Hotel;