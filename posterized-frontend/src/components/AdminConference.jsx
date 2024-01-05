import "./ConferenceList.css"

function AdminConference(props) {
    const {naziv} = props.adminConference;
  
    return (
      <div>
      <p>Naziv: {naziv}</p>
      </div>
    );
  }
  
  export default AdminConference;