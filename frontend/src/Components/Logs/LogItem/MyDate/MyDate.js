import './MyDate.css'
const MyDate = (props) => {
    const month = props.date.toLocaleString('en-us', {month: 'short'});
    const day = props.date.getDate();
    return <div className="date">
       <div className="month">
        {month}
        </div>
       <div className="day">
        {day}
        </div>
    </div>
}
export default MyDate;
