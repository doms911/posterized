import "./ConferenceList.css"

function Conference(props) {
    const {pin, naziv, adminPrezime, adminIme, adminEmail} = props.conference;
  
    return (
      <div>
      <p>Naziv: {naziv}</p>
      <p>Ime admina: {adminIme}</p>
      <p>Prezime admina: {adminPrezime}</p>
      <p>Email admina: {adminEmail}</p>
      <p>Pin: {pin}</p>
      </div>
    );
  }
  
  export default Conference;
  