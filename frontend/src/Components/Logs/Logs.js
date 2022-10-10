import LogItem from "./LogItem/LogItem";
import './Logs.css';

const inputData = [
    {
        date: new Date(2022,3,15,6,45),
        desc: 'basketball',
        time: 150
    },
    {
        date: new Date(2022,6,12,14,45),
        desc: 'work out',
        time: 150
    },
    {
        date: new Date(2022,9,12,21,45),
        desc: 'watch porn',
        time: 200
    }
];
const Logs =()=>{
    return <div className="logs">
        {
            inputData.map(item => <LogItem {...item}/>)
        }
        
        
        </div>

}

export default Logs;

