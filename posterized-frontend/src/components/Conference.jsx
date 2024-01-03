function Conference(props) {
    const {pin, naziv, adminPrezime, adminIme, adminEmail} = props.conference;
  
    return (
      <p>{naziv} {adminPrezime} {adminIme} {adminEmail} {pin}</p>
    );
  }
  
  export default Conference;
  