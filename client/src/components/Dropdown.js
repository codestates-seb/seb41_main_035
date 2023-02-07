// import Select from 'react-select';
/* eslint-disable react/prop-types */
import styled from 'styled-components';
import { useState } from 'react';
import PropTypes from 'prop-types';
const categorylist = ['아우터', '상의', '하의', '원피스', '모자', '신발'];

const Dropdown = ({ onChangeDropdown, itemDropdown }) => {
  // const [selected, setSelected] = useState('');
  // const onchangeSelected = (e) => {
  //   onChangeDropdown(e.target.value);
  // };

  return (
    <SDropdown>
      <form>
        <div className="container">
          {/* select 태그 사용 */}
          <select onChange={onChangeDropdown} value={itemDropdown}>
            {categorylist.map((item) => (
              <option value={item} key={item}>
                {item}
              </option>
            ))}
          </select>
        </div>
      </form>
    </SDropdown>
  );
};

Dropdown.propTypes = {
  onChange: PropTypes.func,
  value: PropTypes.string,
};

const SDropdown = styled.div`
  .container {
    background-color: #eee6ca;
  }
  .container select {
    border: 1.5px solid #d9d4a6;
    border-radius: 3px;
    width: 4.5vw;
  }
`;

export default Dropdown;
