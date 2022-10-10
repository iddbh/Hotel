import MyDate from "./MyDate/MyDate";
import './LogItem.css'
const LogItem = (props) => {
    return <div className="item">
        <MyDate date={props.date}/>
        <div className="content">
            <h2 className="desc">{props.desc}</h2>
            <div className="time">{props.time}mins</div>

        </div>
    </div>
}
export default LogItem;