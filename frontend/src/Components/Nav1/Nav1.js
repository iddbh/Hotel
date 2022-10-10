import styles from './Nav1.module.css'

// 导航栏
const Nav1 = () => {
    /* 整个导航栏 */ 
    return <div className={styles.navBox}>
        {/* 导航栏中放置选项的列表 */}
        <div className={styles.navBox2}>
            <ul>
                <li>
                    <a href='#' className={styles.navContent}>
                        帮助
                    </a>
                </li>
                <li>
                    <a href='#' className={styles.navContent}>
                        常见问题
                    </a>
                </li>
                <li>
                    <a href='#' className={styles.navContent}>
                        历史记录
                    </a>
                </li>
                <li>
                    <a href='#' className={styles.navContent}>
                        登录或注册
                    </a>
                </li>

            </ul>
        </div>
    </div>
}

export default Nav1;