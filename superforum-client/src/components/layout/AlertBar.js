import React, { useContext } from 'react';
import Alert from '@mui/material/Alert';
import AlertContext from '../../context/alert/alertContext';

const AlertBar = () => {
    const alertContext = useContext(AlertContext);
    return (
        alertContext.alerts.length > 0 &&
        alertContext.alerts.map((alert, i) => (
            <Alert severity={alert.type} key={i}>
                {alert.msg}
            </Alert>
        ))
    );
};

export default AlertBar;
